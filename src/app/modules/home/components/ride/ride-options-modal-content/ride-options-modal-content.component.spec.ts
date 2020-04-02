import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RideOptionsModalContentComponent } from './ride-options-modal-content.component';

describe('RideOptionsModalContentComponent', () => {
  let component: RideOptionsModalContentComponent;
  let fixture: ComponentFixture<RideOptionsModalContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RideOptionsModalContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RideOptionsModalContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
