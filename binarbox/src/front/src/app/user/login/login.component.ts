import { Component, OnInit } from '@angular/core';
import {UserService} from '../../_services/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(
      private user: UserService,
      private router: Router
  ) { }

  ngOnInit() {
  }

  public login (formValue) {
      if (!formValue) {
          return;
      }

      this.user.login(formValue).subscribe(data => {
          if (data) {
              if (data['token']) {
                  UserService.setUserToken(data['token']);
                  this.router.navigate(['/']);
              }
          }
      });
  }

}
