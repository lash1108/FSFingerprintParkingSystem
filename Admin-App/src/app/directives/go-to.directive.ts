import {Directive, ElementRef, HostListener, Input} from '@angular/core';
import {Router} from "@angular/router";

@Directive({
  selector: '[appGoTo]'
})
export class GoToDirective {
  @Input() appGoTo: string = '';

  constructor(private router:Router) { }

  @HostListener('click') onClick():void{
    if(this.appGoTo){
      this.router.navigateByUrl(this.appGoTo).then(r => console.log(r));
    }
  }
}
