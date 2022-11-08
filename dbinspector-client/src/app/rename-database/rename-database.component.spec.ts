import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenameDatabaseComponent } from './rename-database.component';

describe('RenameDatabaseSheetComponent', () => {
  let component: RenameDatabaseComponent;
  let fixture: ComponentFixture<RenameDatabaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RenameDatabaseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RenameDatabaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
