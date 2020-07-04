import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { IUserAuthState, IUserAuthResponse } from '../interfaces/i-user-auth-state';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userAuthState: BehaviorSubject<IUserAuthState> = new BehaviorSubject<IUserAuthState>({} as IUserAuthState);
  public userAuth: Observable<IUserAuthState> = this.userAuthState.asObservable();
  public get currentUserAuth(): IUserAuthResponse {
    return this.userAuthState.getValue().authResponse;
  }
  constructor() { }
  setUserAfterLogin(data) {
    this.userAuthState.next({
      isAuthenticated: true,
      authResponse: data
    });
  }
}
