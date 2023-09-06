import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { Fourniseur } from 'src/app/Modules/Fourniseur';
import { Acheteur } from 'src/app/Modules/Acheteur';
import { Operateur } from 'src/app/Modules/Operateur';
import { User } from 'src/app/Services/userService/user';
import {ToastrModule, ToastrService} from 'ngx-toastr';
import { Router } from '@angular/router';
import { DashboardChartsData, IChartProps } from './dashboard-charts-data';
import {AuthService} from "../../Services/authservice/auth.service";


interface IUser {
  name: string;
  state: string;
  registered: string;
  country: string;
  usage: number;
  period: string;
  payment: string;
  activity: string;
  avatar: string;
  status: string;
  color: string;
}

@Component({
  templateUrl: 'dashboard.component.html',
  styleUrls: ['dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  constructor(private chartsData: DashboardChartsData,private Userservice :User,private toastr: ToastrService) {
  }

  public usersF: Fourniseur[] = [];
  public usersO: Acheteur[] = [];
  public usersA: Operateur[] = [];
  public visible = false;
  public UpdateId:number=0  ;

  updatedUserData = {
    gender: '',
    region: '',
    codePostal: '',
    pays: '',
    productType: ''


  };



  public mainChart: IChartProps = {};
  public chart: Array<IChartProps> = [];
  public trafficRadioGroup = new UntypedFormGroup({
    trafficRadio: new UntypedFormControl('Month')
  });

  ngOnInit(): void {

    this.Userservice.GetUsesrByRole('Rôle_Fournisseur').subscribe(
      (data) => {
        this.usersF = data;
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );



    this.Userservice.GetUsesrByRole('Rôle_Opérateur').subscribe(
      (data) => {
        this.usersO = data;
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );




    this.Userservice.GetUsesrByRole('Rôle_Acheteur').subscribe(
      (data) => {
        this.usersA = data;
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );






    this.initCharts();
  }

  initCharts(): void {
    this.mainChart = this.chartsData.mainChart;
  }

  setTrafficPeriod(value: string): void {
    this.trafficRadioGroup.setValue({ trafficRadio: value });
    this.chartsData.initMainChart(value);
    this.initCharts();
  }


  deleteUser(userId: number): void {
    this.Userservice.DeleteUser(userId).subscribe(
      (response) => {
        // Handle success here
        this.toastr.success('User deleted successfully Back !', 'Delete');
        this.ngOnInit()
      },
      (error) => {
        // Handle error here
        console.error('Error deleting user', error);
      }
    );
  }


  UpdateBuyer(): void {


    this.Userservice.updateBuyer(this.UpdateId, this.updatedUserData).subscribe(
      (response) => {
        // Handle success here
        this.ngOnInit()
        this.toastr.success('User updated successfully!', 'Update');

      },
      (error) => {
        // Handle error here

        console.error('Error updating user', error);
      }
    );



  }
  UpdateOperator(): void {


    this.Userservice.updateOperator(this.UpdateId, this.updatedUserData).subscribe(
      (response) => {
        // Handle success here
        this.ngOnInit()
        this.toastr.success('User updated successfully!', 'Update');

      },
      (error) => {
        // Handle error here

        console.error('Error updating user', error);
      }
    );


  }
  UpdateSupplier(): void {


    this.Userservice.updatesupplier(this.UpdateId, this.updatedUserData).subscribe(
      (response) => {
        // Handle success here
        this.ngOnInit()

        this.toastr.success('User updated successfully!', 'Update');

      },
      (error) => {
        // Handle error here

        console.error('Error updating user', error);
      }
    );

    console.log(this.updatedUserData);

  }

  toggleLiveDemo(id:number) {
    this.visible = !this.visible;
    this.UpdateId=id;

  }

  closeLiveDemo() {
    this.visible = !this.visible;

  }
  handleLiveDemoChange(event: any) {
    this.visible = event;
  }


}
