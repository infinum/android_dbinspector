import {Injectable} from '@angular/core';
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";

@Injectable({
  providedIn: 'root'
})
export class AssetService {

  private icons: string[] = ["clear", "drop", "github", "pragma", "table"]

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {
    this.icons.forEach(value => this.register(value))
  }

  registerIcons(): void {
    this.icons.forEach(value => this.register(value))
  }

  private register(icon: string): void {
    this.matIconRegistry.addSvgIcon(
      icon,
      this.domSanitizer.bypassSecurityTrustResourceUrl(`../assets/${icon}.svg`)
    );
  }
}
