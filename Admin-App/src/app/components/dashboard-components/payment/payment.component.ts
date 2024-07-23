import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MyErrorStateMatcher} from "../../util/form/MyErrorStateMatcher";
import {RegistryService} from "../../../services/registry.service";
import Swal from "sweetalert2";
import {Router} from "@angular/router";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {
  labelPosition: 'paypal' | 'creditcard' = 'creditcard';
  tokenusr:string = "";
  usuario:string = "";
  fechaEntrada: string = "";
  horaEntrada: string = "";
  tiempoUso: string = "";
  subtotal: number = 0;
  total: number = 0;
  matcher = new MyErrorStateMatcher();
  formCC:FormGroup;
  formPP:FormGroup;

  constructor(private fb:FormBuilder,
              private registryService:RegistryService,
              private router:Router) {
    this.formCC = this.fb.group({
      creditcardNumber:['',[
        Validators.required,
        Validators.minLength(13),
        Validators.maxLength(19),
        Validators.pattern('^[0-9]*$')
      ]],
      expirationDate: ['', [
        Validators.required,
        Validators.pattern('^(0[1-9]|1[0-2])\\/([0-9]{2})$'),// MM/YY
        Validators.minLength(5),
        Validators.maxLength(5),
      ]],
      ccv: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(4),
        Validators.pattern('^[0-9]{3,4}$') // 3 o 4 dígitos
      ]]
    })
    this.formPP = this.fb.group({
      emailOrPhone: ['', [
        Validators.required,
        Validators.pattern(/^(?:[^\s@]+@[^\s@]+\.[^\s@]+|\+?\d{1,4}[\d\s()-]*)$/), // Expresión regular para correo electrónico o teléfono
        Validators.minLength(5),
        Validators.maxLength(254)
      ]]
    })
  }

  ngOnInit(): void {
    this.populateData();
  }

  getCurrentFormatDate():string{
    const currentDate = new Date();
    const year = currentDate.getFullYear().toString().slice(-2); // Obtiene los últimos 2 dígitos del año
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0'); // Añade 1 al mes y rellena con 0 si es necesario
    const day = currentDate.getDate().toString().padStart(2, '0'); // Rellena con 0 si es necesario
    return `${year}${month}${day}`;
  }

  populateData(): void {
    const registryString = localStorage.getItem('registry');
    if (registryString) {
      try {
        const registry = JSON.parse(registryString);

        // Verificar si 'obj' y 'fecha' existen en el objeto 'registry'
        if (registry && registry.obj && registry.obj.fecha) {
          this.usuario = `${this.getCurrentFormatDate()}${registry.obj.registro.cvereg}`;
          this.tokenusr = registry.obj.registro.tokenusr;
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

  saveAndUnsetRegistry() {
    // creditcard = 1 || PayPal = 2
    if (this.formCC.valid || this.formPP.valid) {
      const data = { name: this.tokenusr };
      console.log(data);

      this.registryService.unsetRegistry(data).subscribe({
        next: (response) => {
          if (response.datos.code === 401) {
            console.log(`Error: ${response.datos.msj}`);
          } else {
            Swal.fire({
              icon: 'success',
              title: '¡Éxito!, pago registrado',
              text: 'Vuelva pronto',
            }).then(() => {
              localStorage.removeItem('fingerprint');
              localStorage.removeItem('registry');
              this.router.navigateByUrl('home').then(() => {});
            });
          }
        },
        error: (error) => {
          console.error('Error en la solicitud:', error);
        },
      });
    } else {
      Swal.fire({
        icon: 'error',
        title: 'Ooops...',
        text: 'Debe completar todos los campos antes de continuar',
      }).then(() => {});
    }
  }


}
