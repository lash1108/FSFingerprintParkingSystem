import {Component, OnInit} from '@angular/core';
import {IdleService} from "../../../services/idle.service";
import Swal from "sweetalert2";
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard-generic-usr',
  templateUrl: './dashboard-generic-usr.component.html',
  styleUrls: ['./dashboard-generic-usr.component.scss']
})
export class DashboardGenericUsrComponent implements OnInit {
  fechaEntrada: string = "";
  horaEntrada: string = "";
  tiempoUso: string = "";
  subtotal: number = 0;
  total: number = 0;

  constructor(private idleService: IdleService, private router:Router) {
  }

  ngOnInit(): void {
    this.idleService.initialize('fingerprint');
    this.populateData();
    //this.openAdvice()
  }

  populateData(): void {
    const registryString = localStorage.getItem('registry');
    if (registryString) {
      try {
        const registry = JSON.parse(registryString);

        // Verificar si 'obj' y 'fecha' existen en el objeto 'registry'
        if (registry && registry.obj && registry.obj.fecha) {
          this.fechaEntrada = registry.obj.fecha.value1;
          this.horaEntrada = registry.obj.fecha.value2;
          this.tiempoUso = registry.obj.duracion.name;
          this.subtotal = registry.obj.subTotal;
          this.total = registry.obj.total;
        } else {
          console.error('Registry object is missing expected properties.');
        }
      } catch (e) {
        console.error('Error parsing registry data:', e);
      }
    } else {
      console.error('No registry data found in localStorage.');
    }
  }

  deleteLocalStorage(): void {
    if (localStorage.getItem('registry') != null &&
      localStorage.getItem('fingerprint') != null) {
      localStorage.removeItem('registry');
      localStorage.removeItem('fingerprint');
    }
  }

  openAdvice(){
    Swal.fire({
      icon: 'info',
      title: 'Usuario Pensionado',
      html: '¿Desea añadir más de un auto y recibir un precio preferencial? <br> <strong>Regístrese como usuario pensionado.</strong>',
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: 'Sí, registrar',
      denyButtonText: 'No, gracias',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
      } else if (result.isDenied) {
        this.router.navigateByUrl('payment').then(() => null)
      }
    });
    return
  }
}

