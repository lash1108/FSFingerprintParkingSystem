import {Component, OnInit} from '@angular/core';
import {IdleService} from "../../../services/idle.service";

@Component({
  selector: 'app-dashboard-generic',
  templateUrl: './dashboard-generic.component.html',
  styleUrls: ['./dashboard-generic.component.scss']
})
export class DashboardGenericComponent implements OnInit {

  constructor(private idleService:IdleService) {
  }

  ngOnInit(): void {
    this.idleService.initialize('fingerprint')
  }

}
