import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DatabasesComponent} from './databases/databases.component';
import {MatCardModule} from "@angular/material/card";
import {DeleteDatabaseComponent} from './delete-database/delete-database.component';
import {MatListModule} from "@angular/material/list";
import {RenameDatabaseComponent} from './rename-database/rename-database.component';
import {MatLineModule, MatRippleModule} from "@angular/material/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {SchemaComponent} from './schema/schema.component';
import {MatTabsModule} from "@angular/material/tabs";
import {TableComponent} from './table/table.component';
import {MatTableModule} from "@angular/material/table";
import {ViewComponent} from './view/view.component';
import {TriggerComponent} from './trigger/trigger.component';
import {MatDialogModule} from "@angular/material/dialog";
import {PragmaComponent} from './pragma/pragma.component';
import {FlexLayoutModule} from "@angular/flex-layout";

@NgModule({
  declarations: [
    AppComponent,
    DatabasesComponent,
    DeleteDatabaseComponent,
    RenameDatabaseComponent,
    SchemaComponent,
    TableComponent,
    ViewComponent,
    TriggerComponent,
    PragmaComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatLineModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatTabsModule,
    MatListModule,
    MatRippleModule,
    MatTableModule,
    MatDialogModule,
    FlexLayoutModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
