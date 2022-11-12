import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PragmaComponent } from './pragma.component';

describe('PragmaComponent', () => {
  let component: PragmaComponent;
  let fixture: ComponentFixture<PragmaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PragmaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PragmaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
