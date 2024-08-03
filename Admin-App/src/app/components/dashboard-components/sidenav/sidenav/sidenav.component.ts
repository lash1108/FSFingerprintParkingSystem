import {animate, keyframes, style, transition, trigger,} from '@angular/animations';
import {Component, EventEmitter, HostListener, OnInit, Output,} from '@angular/core';

import {navBarData} from "./nav-data";

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({opacity: 0}),
        animate('350ms', style({opacity: 1})),
      ]),
      transition(':leave', [
        style({opacity: 1}),
        animate('350ms', style({opacity: 0})),
      ]),
    ]),
    trigger('rotate', [
      transition(':enter', [
        animate(
          '1000ms',
          keyframes([
            style({transform: 'rotate(0deg)', offset: '0'}),
            style({transform: 'rotate(2turn)', offset: '1'}),
          ])
        ),
      ]),
    ]),
  ],
})
export class SidenavComponent implements OnInit {

  @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = false;
  screenWidth = 0;
  navData = navBarData;

  constructor() {
  }

  @HostListener('window:resize', ['$event']) onResize(event: any) {
    this.screenWidth = window.innerWidth;
    if (this.screenWidth <= 768) {
      this.collapsed = false;
      this.onToggleSideNav.emit({
        collapsed: this.collapsed,
        screenWidth: this.screenWidth,
      });
    }
  }

  ngOnInit(): void {
    this.screenWidth = window.innerWidth;
  }

  toggleCollapse(): void {
    this.collapsed = !this.collapsed;
    this.onToggleSideNav.emit({
      collapsed: this.collapsed,
      screenWidth: this.screenWidth,
    });
  }

  getFilteredNavData() {
    // Obtén el valor de 'typeusr' y conviértelo a número, o usa un valor por defecto (por ejemplo, -1)
    const typeusr = Number(localStorage.getItem('typeusr')) || -1;

    if (typeusr === 0) {
      return this.navData.filter(value => value.section === 'main');
    } else if (typeusr === 1) {
      return this.navData.filter(value => value.section === 'main' && value.type === 'usr');
    } else {
      // Opcional: Maneja el caso cuando 'typeusr' no es 0 ni 1
      return this.navData.filter(value => value.section === 'main');
    }
  }



}
