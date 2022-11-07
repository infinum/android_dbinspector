import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SchemaComponent} from "./schema/schema.component";
import {DatabasesComponent} from "./databases/databases.component";
import {TableComponent} from "./table/table.component";
import {ViewComponent} from "./view/view.component";
import {TriggerComponent} from "./trigger/trigger.component";

const routes: Routes = [
  {path: '', redirectTo: '/databases', pathMatch: 'full'},
  {path: 'databases', component: DatabasesComponent},
  {path: 'databases/:database_id/schema', component: SchemaComponent},
  {path: 'databases/:database_id/tables/:schema_id', component: TableComponent},
  {path: 'databases/:database_id/views/:schema_id', component: ViewComponent},
  {path: 'databases/:database_id/triggers/:schema_id', component: TriggerComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
