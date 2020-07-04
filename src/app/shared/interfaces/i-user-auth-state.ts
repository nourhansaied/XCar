export interface IUserAuthState {
    isAuthenticated: boolean;
    authResponse: IUserAuthResponse | null;
}
export interface IUserAuthResponse {
    token: string;
}