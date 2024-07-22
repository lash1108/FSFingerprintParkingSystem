import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from "../../util/form/MyErrorStateMatcher";
import { UserService } from "../../../services/user.service";
import { emailAsyncValidator, usernameAsyncValidator } from "../../util/form/Validators";
import Swal from "sweetalert2";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  form: FormGroup;
  matcher = new MyErrorStateMatcher();
  showPassword: boolean = false;
  validForm: boolean = false;

  constructor(private userService: UserService,
              private fb: FormBuilder,
              public dialogRef: MatDialogRef<SignupComponent>) {
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
  }

  ngOnInit(): void {
    // Suscribirse a los cambios de estado del formulario
    this.form.statusChanges.subscribe(status => {
      this.validForm = status === 'VALID';
      console.log(status);
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  unsetTokenFromLocalStorage(): void {
    localStorage.removeItem('fingerprint');
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
                this.unsetTokenFromLocalStorage();
                this.dialogRef.close(); // Cierra el diálogo en caso de éxito
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
}
