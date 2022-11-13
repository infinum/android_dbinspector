import {Component} from '@angular/core';
import {AssetService} from "./asset.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'dbinspector-client';

  constructor(
    private assetService: AssetService
  ) {
    assetService.registerIcons()
  }
}

/*
TODO: Search everywhere
TODO: Pagination everywhere
TODO: Dark theme and toggle and initial getter onInit
 */
