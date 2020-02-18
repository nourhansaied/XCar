import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { HomeComponent } from './modules/home/home/home.component';

const routes: Routes = [
  {
    path: "home", 
    children: [
      { path: "", loadChildren: "./modules/home/home.module#HomeModule" }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
