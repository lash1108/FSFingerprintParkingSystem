import { AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import {Observable, of} from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { UserService } from '../../../services/user.service';

export function emailAsyncValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) {
      return of(null);
    }

    return userService.validEmail({ name: control.value }).pipe(
      map(response => {
        // Verifica la disponibilidad del email
        return response.value === 200 ? null : { emailTaken: true };
      }),
      catchError(() => of({ emailTaken: true })) // Maneja errores del servicio
    );
  };
}

export function usernameAsyncValidator(userService: UserService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    if (!control.value) {
      return of(null); // Si el campo está vacío, no hay errores de validación.
    }

    return userService.validLogin({ name: control.value }).pipe(
      map(response => {
        // Verifica la disponibilidad del nombre de usuario
        return response.value === 200 ? null : { usernameTaken: true };
      }),
      catchError(() => of({ usernameTaken: true })) // Maneja errores del servicio
    );
  };
}
