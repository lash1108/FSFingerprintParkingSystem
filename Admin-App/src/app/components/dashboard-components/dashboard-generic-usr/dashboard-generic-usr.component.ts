import {Component, OnInit} from '@angular/core';
import {IdleService} from "../../../services/idle.service";

@Component({
  selector: 'app-dashboard-generic-usr',
  templateUrl: './dashboard-generic-usr.component.html',
  styleUrls: ['./dashboard-generic-usr.component.scss']
})
export class DashboardGenericUsrComponent implements OnInit {

  constructor(private idleService:IdleService) {}

  ngOnInit(): void {
    this.idleService.initialize('fingerprint')
  }

}
