import { Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { fromEvent, Observable, merge, timer } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IdleService {
  private inactivityTimeout = 600000;
  private idle$: Observable<Event> = new Observable<Event>();

  constructor(private router: Router, private ngZone: NgZone) { }

  public initialize(localitmRemove?: string): void {
    // Detectar actividad del usuario
    this.idle$ = merge(
      fromEvent(document, 'mousemove'),
      fromEvent(document, 'keydown'),
      fromEvent(document, 'scroll')
    );

    // Configurar el temporizador de inactividad
    this.ngZone.runOutsideAngular(() => {
      this.idle$
        .pipe(
          switchMap(() => timer(this.inactivityTimeout)),
          tap(() => {
            this.ngZone.run(() => {
              // Primero, guardar los datos antes de eliminar cualquier cosa
              this.saveBeforeLogout();

              // Eliminar el ítem de localStorage al momento de redirigir
              this.deleteLocalStorage(localitmRemove);

              // Redirigir a la página de inicio
              this.router.navigateByUrl('home').then(() => null);
            });
          })
        )
        .subscribe();
    });
  }

  private saveBeforeLogout(): void {
    // Implementa cualquier lógica para guardar datos antes de redirigir
    // Ejemplo:
    const registryData = localStorage.getItem('registry');
    if (registryData) {
      // Por ejemplo, podrías guardar el registro en otro lugar o hacer una llamada a una API
      console.log('Registry Data Before Logout:', registryData);
    }
  }

  private deleteLocalStorage(localitmRemove?: string): void {
    if (localitmRemove && localStorage.getItem(localitmRemove)) {
      localStorage.removeItem(localitmRemove);
    }

    if (localStorage.getItem('registry')) {
      localStorage.removeItem('registry');
    }
  }


}

