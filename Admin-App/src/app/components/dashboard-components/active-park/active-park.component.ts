import {Component, OnInit} from '@angular/core';
import {EstacionamientoService} from "../../../services/estacionamiento.service";
import {Estacionamiento} from "../../../model/models";
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-active-park',
  templateUrl: './active-park.component.html',
  styleUrls: ['./active-park.component.scss']
})
export class ActiveParkComponent implements OnInit {
  typeusr:number = 0;
  cveusr:number = 0;
  dataFromService: any[] = [];
  displayedColumns: string[] =
    ['idcar','entryDate', 'entryTime', 'duracion', 'subTotal', 'total'];
  dataSource = new MatTableDataSource<Estacionamiento>();


  constructor(private estService:EstacionamientoService) {
  }

  ngOnInit(): void {
    this.validatePageType();
  }

  validatePageType(): void {
    const storedTypeValue = localStorage.getItem('typeusr');
    const storedCveusrValue = localStorage.getItem('cveusr');

    if (storedTypeValue !== null && storedCveusrValue !== null) {
      const pageType = parseInt(storedTypeValue, 10);
      //const cveusuario = parseInt(storedCveusrValue, 10);
      this.typeusr = pageType;
      if (pageType == 1) {
        this.findActiveEstacionamientos(storedCveusrValue);
      }
    } else {
      console.warn('No se encontró ningún valor para typeusr en localStorage');
    }
  }


  findActiveEstacionamientos(key?: string):void {
    const data: { datos: { page?:number, size?:number, key?: string, type: number } }
      = { datos: { page:0,size:10,type: 2 } };

    if (key) {
      data.datos.key = key;
    }

    this.estService.findEstacionamientos(data)
      .subscribe({
        next: (response) => {
          // Accede al array principal de datos
          console.log(response)
          const ests = response.datos.data;

          // Aquí guardamos todos los objetos dentro de `data` en `dataFromService`
          this.dataFromService = ests.flatMap((est: { data: any; }) => est.data);
          this.dataSource.data = ests.flatMap((est: { data: any; }) => est.data);

          // Opcional: Imprimir los datos para verificar
          console.log(this.dataFromService);
        }
      });

  }


}
