import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { FilmeComponent } from './components/filme/filme.component';
import { TodolistComponent } from './components/todolist/todolist.component';

const routes: Routes = [
  { path: 'test', component: TodolistComponent },
  { path: 'test2', component: FilmeComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
