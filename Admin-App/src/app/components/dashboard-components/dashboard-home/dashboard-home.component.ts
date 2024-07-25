import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';
import {GlobalService} from "../../../services/global.service";
import Swal from "sweetalert2";
import {EstacionamientoService} from "../../../services/estacionamiento.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MyErrorStateMatcher} from "../../util/form/MyErrorStateMatcher";
import {UserService} from "../../../services/user.service";


@Component({
  selector: 'app-dashboard-home',
  templateUrl: './dashboard-home.component.html',
  styleUrls: ['./dashboard-home.component.scss']
})
export class DashboardHomeComponent implements OnInit, AfterViewInit {
  @ViewChild('icon') icon: any;
  typeUsr: number = 0;
  cveusr: number = 0;
  nameusr:string = "";
  loginusr:string="";
  emailusr:string = "";
  idcar:[] = [];
  idcars: string[] = [];
  formCarPlate: FormGroup;
  tokenusr:string|null = "" ;
  matcher = new MyErrorStateMatcher();

  constructor(private globalService: GlobalService,
              private renderer: Renderer2,
              private elRef:ElementRef,
              private fb: FormBuilder,
              private estService:EstacionamientoService,
              private userService:UserService) {
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
    this.globalService.setTypeUser(0);
    this.typeUsr = this.globalService.getTypeUser();
    this.populateData();
  }

  ngAfterViewInit(): void {
  }

  populateData(): void {
    const responseUserStr = localStorage.getItem('responseUser');

    if (responseUserStr) {
      const responseUser = JSON.parse(responseUserStr);
      console.log(responseUser.datos);
      const keys = Object.keys(responseUser.datos);
      const key = parseInt(keys[0]);
      this.cveusr = key;
      const userData = responseUser.datos[key];
      this.nameusr = userData.nameusr;
      this.loginusr = userData.loginusr;
      this.emailusr = userData.emailusr;
      this.idcar = userData.idcar;
      this.idcars = this.idcar.slice(0, 4);
      this.tokenusr = userData.tokenusr;
      if (this.idcar.length==0){
        this.showCarPlatePrompt().then(() => null);
      }
      //this.typeUsr = userData.typeusr;


    }
  }

  changeStyleBasedOnUserType(): void {

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
      allowEscapeKey:false,
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
      } catch (error) {
        await Swal.fire({
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
        cveusr: this.cveusr,
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
            }).then(() => {});
          } else {
            Swal.fire({
              icon: "success",
              title: "Exito",
              text: "Usuario Creado correctamente",
            }).then(() => {
              if (this.tokenusr != null) {
                localStorage.removeItem('responseUser');
                this.validateUsrByToken(this.tokenusr).then(() => null);
              }
            });
          }
        },
        error: (error) => {
          console.error('Error en la solicitud:', error);
        }
      });
  }

  async validateUsrByToken(token: string): Promise<boolean> {
    const data = {
      name: token
    };
    console.log(` datos ${JSON.stringify(data)}`)
    try {
      const response = await this.userService.validUsrByToken(data).toPromise();
      if (response.datos.codigo === 404) {
        return false;
      } else {
        localStorage.setItem('responseUser', JSON.stringify(response));
        location.reload()
        return true;
      }
    } catch (error) {
      return false;
    }
  }

}
