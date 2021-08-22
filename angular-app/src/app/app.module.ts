import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { FooterComponent, FooterComponentSetServerDialog } from './components/footer/footer.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SidebarModule } from 'ng-sidebar';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { MatNativeDateModule, MatRippleModule } from '@angular/material/core';
import { A11yModule } from '@angular/cdk/a11y';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatBadgeModule } from '@angular/material/badge';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSliderModule } from '@angular/material/slider';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTreeModule } from '@angular/material/tree';
import { PortalModule } from '@angular/cdk/portal';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { JasmartyConfigComponent } from './components/jasmartyConfig/jasmartyConfig.component';
import { JasmartyPagesComponent, JasmartyPagesComponentGoToDialog } from './components/jasmartyPages/jasmartyPages.component';
import { JasmartyActionsComponent, JasmartyActionsComponentDialog } from './components/jasmartyActions/jasmartyActions.component';
import { MatStepperModule } from '@angular/material/stepper';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { JasmartyFullViewComponent } from './components/jasmartyFullView/jasmartyFullView.component';
import { JasmartyMainComponent } from './components/jasmartyMain/jasmartyMain.component';
import { TelegramConfigComponent } from './components/telegramConfig/telegramConfig.component';
import { CryptComponent } from './components/crypt/crypt.component';
import { TelegramChatComponent } from './components/telegramChat/telegramChat.component';
import { TelegramMsgComponent, TelegramMsgComponentDialogComponent } from './components/telegramMsg/telegramMsg.component';
import { TelegramLogComponent } from './components/telegramLog/telegramLog.component';
import { TodolistComponent, TodolistComponentDialogComponent } from './components/todolist/todolist.component';
import { FilmeComponent, FilmeComponentDialog } from './components/filme/filme.component';
import { WipfUserVwComponent, WipfUserVWComponentDialogComponent } from './components/wipfUserVw/wipfUserVw.component';
import { Jasmarty12864PanelComponent } from './components/jasmarty12864Panel/jasmarty12864Panel.component';
import { Jasmarty12864PagesComponent } from './components/jasmarty12864Pages/jasmarty12864Pages.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxPhotoEditorModule } from 'ngx-photo-editor';
import { DialogJaNeinComponent, DialogWartenComponent } from './dialog/main.dialog';
import { AppRoutingModule } from './app-routing.module';
import { CharGeneratorComponent } from './components/charGenerator/charGenerator.component';
import { DebugSeiteComponent } from './components/debugSeite/debugSeite.component';

@NgModule({
  declarations: [
    AppComponent,
    JasmartyPagesComponent,
    JasmartyPagesComponentGoToDialog,
    JasmartyActionsComponent,
    JasmartyActionsComponentDialog,
    JasmartyConfigComponent,
    JasmartyMainComponent,
    JasmartyFullViewComponent,
    Jasmarty12864PanelComponent,
    Jasmarty12864PagesComponent,
    TelegramConfigComponent,
    FooterComponent,
    FooterComponentSetServerDialog,
    TodolistComponent,
    TodolistComponentDialogComponent,
    FilmeComponent,
    FilmeComponentDialog,
    CryptComponent,
    TelegramChatComponent,
    TelegramMsgComponent,
    TelegramMsgComponentDialogComponent,
    TelegramLogComponent,
    WipfUserVwComponent,
    WipfUserVWComponentDialogComponent,
    DialogJaNeinComponent,
    DialogWartenComponent,
    JasmartyActionsComponentDialog,
    CharGeneratorComponent,
    DebugSeiteComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    SidebarModule.forRoot(),
    PerfectScrollbarModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    A11yModule,
    ClipboardModule,
    CdkStepperModule,
    CdkTableModule,
    CdkTreeModule,
    DragDropModule,
    NgxPhotoEditorModule,
    MatAutocompleteModule,
    MatBadgeModule,
    MatBottomSheetModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    MatTreeModule,
    PortalModule,
    ScrollingModule,
    MatStepperModule,
    MatDatepickerModule,
    NgbModule,
  ],
  providers: [{ provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } }],
  bootstrap: [AppComponent],
  entryComponents: [AppComponent],
})
export class AppModule { }
