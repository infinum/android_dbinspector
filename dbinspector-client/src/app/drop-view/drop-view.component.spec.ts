import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropViewComponent } from './drop-view.component';

describe('DropViewComponent', () => {
  let component: DropViewComponent;
  let fixture: ComponentFixture<DropViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DropViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DropViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
