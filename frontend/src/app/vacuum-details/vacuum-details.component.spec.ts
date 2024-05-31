import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VacuumDetailsComponent } from './vacuum-details.component';

describe('VacuumDetailsComponent', () => {
  let component: VacuumDetailsComponent;
  let fixture: ComponentFixture<VacuumDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VacuumDetailsComponent]
    });
    fixture = TestBed.createComponent(VacuumDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
