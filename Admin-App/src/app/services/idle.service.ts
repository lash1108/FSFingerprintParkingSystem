import { Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { fromEvent, Observable, merge, timer } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IdleService {
  private inactivityTimeout = 60000;
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
              // Eliminar el ítem de localStorage al momento de redirigir
              if (localitmRemove && localStorage.getItem(localitmRemove)) {
                localStorage.removeItem(localitmRemove);
              }
              // Redirigir a la página de inicio
              this.router.navigateByUrl('home');
            });
          })
        )
        .subscribe();
    });
  }
}
