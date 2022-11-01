import {Component, OnInit} from '@angular/core';
import {DatabaseService} from "../database.service";
import {Database} from "../database";

@Component({
  selector: 'app-databases',
  templateUrl: './databases.component.html',
  styleUrls: ['./databases.component.css']
})
export class DatabasesComponent implements OnInit {

  databases: Database[] = [];

  constructor(
    private databaseService: DatabaseService
  ) {
  }

  ngOnInit(): void {
    this.databaseService.databases()
      .subscribe(
        databases => this.databases = databases
      )
  }
}
