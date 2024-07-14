import {Component} from '@angular/core';
import {Router} from '@angular/router';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-sidenav-util',
  templateUrl: './sidenav-util.component.html',
  styleUrls: ['./sidenav-util.component.scss']
})
export class SidenavUtilComponent {

  isSideNavCollapsed = false;
  screenWidth = 0;

  constructor(private router: Router) {
  }

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  goToComponent(link: string) {
    this.router.navigate([`/${link}`]);
  }
}
