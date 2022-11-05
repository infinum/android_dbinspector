import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenameDatabaseSheetComponent } from './rename-database-sheet.component';

describe('RenameDatabaseSheetComponent', () => {
  let component: RenameDatabaseSheetComponent;
  let fixture: ComponentFixture<RenameDatabaseSheetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RenameDatabaseSheetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RenameDatabaseSheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
