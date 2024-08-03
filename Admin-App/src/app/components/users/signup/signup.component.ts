import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from "../../util/form/MyErrorStateMatcher";
import { UserService } from "../../../services/user.service";
import { emailAsyncValidator, usernameAsyncValidator } from "../../util/form/Validators";
import Swal from "sweetalert2";
import {MatDialogRef} from "@angular/material/dialog";
import {EstacionamientoService} from "../../../services/estacionamiento.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  form: FormGroup;
  formCarPlate: FormGroup;
  matcher = new MyErrorStateMatcher();
  showPassword: boolean = false;
  validForm: boolean = false;
  cveusr: any;

  constructor(private userService: UserService,
              private fb: FormBuilder,
              private estService:EstacionamientoService,
              private router:Router,
              public dialogRef: MatDialogRef<SignupComponent>,) {
    this.form = this.fb.group({
      email: ['', [
          Validators.required,
          Validators.email,
          Validators.pattern(/^[^\s@]+@[^\s@]+\.[^\s@]+$/), // Expresión regular básica
          Validators.minLength(5),
          Validators.maxLength(254)
        ],
        [emailAsyncValidator(this.userService)]
      ],
      username: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(20),
        Validators.pattern('^[a-zA-Z0-9_]*$')
      ], [usernameAsyncValidator(this.userService)]],
      name: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z]+(?: [a-zA-Z]+)*$')
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')
      ]]
    });
    this.formCarPlate = this.fb.group({
      carPlate: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(7),
        Validators.pattern('^[A-Z0-9-]*$')
      ]]
    });
  }

  ngOnInit(): void {
    this.showDialogFingerPrint().then(() => null);
  }

  async showDialogFingerPrint(): Promise<void> {
    await Swal.fire({
      title: 'No se ha registrado una huella',
      text: 'Por favor, use el lector y espere hasta que se registre una huella.',
      icon: 'warning',
      showCancelButton: false,
      showConfirmButton: false,
      allowOutsideClick: false, // No permite cerrar la alerta haciendo clic fuera de ella
      allowEscapeKey: false, // No permite cerrar la alerta con la tecla Escape
      didOpen: () => {
        // Aquí puedes configurar tu lector de huellas y realizar una verificación continua
        const interval = setInterval(async () => {
          const fingerprint = localStorage.getItem('fingerprint');
          if (fingerprint) {
            const validUser = await this.userService.validateUsrByToken(fingerprint);
            if (validUser){
              await this.showUsrError(fingerprint);
            }else{
              clearInterval(interval); // Detener la verificación continua
              Swal.close(); // Cerrar el diálogo
            }
          }
        }, 1000); // Verificar cada segundo
      }
    });
  }

  async showUsrError(token: string): Promise<void> {
    await Swal.fire({
      title: 'Error',
      text: 'Se ha encontrado un usuario con este token. El almacenamiento de huellas será reiniciado.',
      icon: 'error',
      confirmButtonText: 'Aceptar'
    });

      // Borra el fingerprint del localStorage
      localStorage.removeItem('fingerprint');

      // Vuelve a mostrar el diálogo de huella
      await this.showDialogFingerPrint();
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  unsetTokenFromLocalStorage(): void {
    localStorage.removeItem('fingerprint');
    localStorage.removeItem('iscreating');
  }

  saveUser(): void {
    if (this.form.valid) {
      const formValues = this.form.value;

      const data = {
        datos: {
          email: formValues.email,
          login: formValues.username,
          idcar: [],
          nameusr: formValues.name,
          password: formValues.password,
          token: localStorage.getItem('fingerprint'),
          typeusr: "1"
        }
      };

      console.log('Form data:', this.form.value);
      this.userService.createOrUpdateUsr(data)
        .subscribe({
          next: (response) => {
            if (response.datos.code === 401) {
              Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Something went wrong!",
              }).then(() => {
                this.unsetTokenFromLocalStorage();
                this.dialogRef.close(); // Cierra el diálogo en caso de error
              });
            } else {
              Swal.fire({
                icon: "success",
                title: "Exito",
                text: "Usuario Creado correctamente",
              }).then(() => {
                if (localStorage.getItem('iscreating')){
                  console.log(response.datos)
                  this.cveusr = response.datos.cveUsr;
                  this.showCarPlatePrompt().then(() => this.dialogRef.close());
                }else{
                  this.unsetTokenFromLocalStorage();
                  this.dialogRef.close(); // Cierra el diálogo en caso de éxito
                }
              });
            }
          },
          error: (error) => {
            console.error('Error en la solicitud:', error);
          }
        });
    } else {
      console.log('Form is invalid');
    }
  }

  async showCarPlatePrompt() {
    const { value: carPlate } = await Swal.fire({
      title: "Para continuar, por favor introduzca la placa de su auto",
      input: "text",
      inputAttributes: {
        autocapitalize: "true"
      },
      showCancelButton: false,
      confirmButtonText: "Continuar",
      showLoaderOnConfirm: true,
      allowEscapeKey:true,
      allowOutsideClick: false,


      preConfirm: async (carPlate) => {
        // Validación del valor introducido
        const carPlateControl = this.formCarPlate.get('carPlate');
        carPlateControl?.setValue(carPlate);
        carPlateControl?.markAsTouched();
        if (!carPlateControl?.valid) {
          Swal.showValidationMessage('La placa del auto no es válida. Asegúrate de que cumple con los requisitos.');
          return false; // Evita que el SweetAlert se cierre
        }
        return carPlate;
      },
    });

    if (carPlate) {
      try {
        // Llama al servicio para guardar la placa del auto
        await this.saveCarPlate(carPlate);
        Swal.fire({
          icon: 'success',
          title: '¡Éxito!',
          text: 'Placa de auto registrada correctamente',
        }).then(() => {
          localStorage.removeItem('iscreating');
          //this.router.navigateByUrl('home');
        });
      } catch (error) {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: `No se pudo registrar la placa`,
        });
      }
    }
  }

  async saveCarPlate(carPlate: string): Promise<void> {
    const data = {
      datos: {
        cveusr: this.cveusr, // Asumiendo que `this.cveusr` es un número
        placa: carPlate
      }
    };

    this.estService.createOrUpdateEst(data)
      .subscribe({
        next: (response) => {
          if (response.datos.code === 401) {
            Swal.fire({
              icon: "error",
              title: "Oops...",
              text: "Something went wrong!",
            }).then(() => {
              this.unsetTokenFromLocalStorage();
              this.dialogRef.close(); // Cierra el diálogo en caso de error
            });
          } else {
            Swal.fire({
              icon: "success",
              title: "Exito",
              text: "Usuario Creado correctamente",
            }).then(() => {
              localStorage.removeItem('fingerprint');
              localStorage.removeItem('registry');
              this.router.navigateByUrl('home')
            });
          }
        },
        error: (error) => {
          console.error('Error en la solicitud:', error);
        }
      });
  }

}
