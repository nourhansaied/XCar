import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { HomeComponent } from "./modules/home/home/home.component";

const routes: Routes = [
  {
    path: "",
    redirectTo: "home",
    pathMatch: "full"

  },
  {
    path: "home",
    loadChildren: "./modules/home/home.module#HomeModule"
  },
  {
    path: "car",
    loadChildren: "./modules/car/car.module#CarModule",
  },
  {
    path: "login",
    loadChildren: "./core/auth/login/login.module#LoginModule",
  },
  {
    path: "signup",
    loadChildren: "./core/auth/signup/signup.module#SignupModule",
  },
  {
    path: "me",
    loadChildren: "./modules/profile/profile.module#ProfileModule",
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule { }
