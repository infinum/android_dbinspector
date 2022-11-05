import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SchemaComponent} from "./schema/schema.component";
import {DatabasesComponent} from "./databases/databases.component";

const routes: Routes = [
  {path: '', redirectTo: '/databases', pathMatch: 'full'},
  {path: 'databases', component: DatabasesComponent},
  {path: 'databases/:id/schema', component: SchemaComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
