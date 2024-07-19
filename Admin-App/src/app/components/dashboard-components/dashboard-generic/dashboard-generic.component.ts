import {Component, OnInit} from '@angular/core';
import {IdleService} from "../../../services/idle.service";
import {RegistryService} from "../../../services/registry.service";

@Component({
  selector: 'app-dashboard-generic',
  templateUrl: './dashboard-generic.component.html',
  styleUrls: ['./dashboard-generic.component.scss']
})
export class DashboardGenericComponent implements OnInit {
  token: number = 0;
  data: { name?: string } = {};
  fechaEntrada:string = "";
  horaEntrada:string = "";

  constructor(
    private idleService: IdleService,
    private registryService: RegistryService
  ) { }

  ngOnInit(): void {
    this.idleService.initialize('fingerprint');
    const fingerprint = localStorage.getItem('fingerprint');
    if (fingerprint) {
      this.token = Number(fingerprint);
    }
    this.setRegistry(this.token);

    this.populateData();
  }

  setRegistry(token: number): void {
    this.data = {
      name: `${token}`
    };

    this.registryService.setNewRegistry(this.data).subscribe(
      response => {
        if (response.datos.code === 200){
          console.log('Registro exitoso:', response);
          localStorage.setItem('registry', JSON.stringify(response.datos));
          this.ngOnInit()
        }
      },
      error => {
        console.error('Error al registrar:', error);
      }
    );
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

  deleteLocalStorage():void{
    if (localStorage.getItem('registry')!=null &&
      localStorage.getItem('fingerprint')!=null){
      localStorage.removeItem('registry');
      localStorage.removeItem('fingerprint');
    }
  }
}
