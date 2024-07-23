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
    // Suscribirse a los cambios de estado del formulario
    this.form.statusChanges.subscribe(status => {
      this.validForm = status === 'VALID';
      console.log(status);
    });

    /*if (localStorage.getItem('iscreating')) {
      this.showCarPlatePrompt();
    }*/
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
