import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { HomeComponent } from "./modules/home/home/home.component";

const routes: Routes = [
  // {
  //   path: "",
  //   component: HomeComponent,
  //   children: [
  //     // { path: "", loadChildren: "./modules/home/home.module#HomeModule" },
  //     { path: "driver", loadChildren: "./core/components/driver/driver.module#DriverModule" },

  //   ]
  // },

  { path: "", component: HomeComponent },
  {
    path: "car",
    loadChildren: "./modules/car/car.module#CarModule",
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
