import { Component, OnInit, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { GlobalService } from "../../services/global.service";

@Component({
  selector: 'app-dashboard-home',
  templateUrl: './dashboard-home.component.html',
  styleUrls: ['./dashboard-home.component.scss']
})
export class DashboardHomeComponent implements OnInit {
  @ViewChild('icon') icon: any;

  constructor(private globalService: GlobalService,
              private renderer: Renderer2) { }

  typeUsr: number = 0;

  ngOnInit(): void {
    this.globalService.setTypeUser(0);
    this.typeUsr = this.globalService.getTypeUser();
    //---//
    console.log(this.globalService.getTypeUser());
    this.changeStyleBasedOnUserType();
  }

  changeStyleBasedOnUserType(): void {

  }
}
