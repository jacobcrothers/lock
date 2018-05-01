import { Component, OnInit } from '@angular/core';
import {UserService} from '../../_services/user.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  constructor(private user: UserService) { }

  ngOnInit() {
  }

  forgotPassword (form) {
      if (!form || !form['email']) {
          return;
      }

      this.user.forgotPassword(form);
  }
}
