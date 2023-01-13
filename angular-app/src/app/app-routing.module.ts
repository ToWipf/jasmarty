import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DebugSeiteComponent } from './components/_debug/debugSeite/debugSeite.component';
import { MainmenueComponent } from './components/_main/mainmenue/mainmenue.component';

const routes: Routes = [
  { path: '', component: MainmenueComponent },
  { path: 'test', component: DebugSeiteComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {
}
