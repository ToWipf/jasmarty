import { A11yModule } from '@angular/cdk/a11y';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { CharGeneratorComponent } from './components/charGenerator/charGenerator.component';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { CryptComponent } from './components/crypt/crypt.component';
import { DayLogComponent, DaylogComponentDialogDayComponent, DaylogComponentDialogTypeComponent, DaylogComponentDialogTypeListComponent } from './components/daylog/daylog.component';
import { DaylogComponentEventlist, DaylogComponentDialogEventComponent } from './components/daylog/daylog.eventlist';
import { DebugSeiteComponent } from './components/debugSeite/debugSeite.component';
import { DialogJaNeinComponent, DialogVariablenHilfeComponent, DialogWartenComponent } from './dialog/main.dialog';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { DynpagesComponent, DynpagesComponentDialogComponent } from './components/dynpages/dynpages.component';
import { DynpageShowComponent } from './components/dynpageShow/dynpageShow.component';
import { FileVwComponent } from './components/fileVw/fileVw.component';
import { FilmeComponent, FilmeComponentDialog } from './components/filme/filme.component';
import { FooterComponent, FooterComponentSetServerDialog } from './components/footer/footer.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Jasmarty12864PagesComponent } from './components/jasmarty12864Pages/jasmarty12864Pages.component';
import { Jasmarty12864PanelComponent } from './components/jasmarty12864Panel/jasmarty12864Panel.component';
import { JasmartyActionsComponent, JasmartyActionsComponentDialog } from './components/jasmartyActions/jasmartyActions.component';
import { JasmartyConfigComponent } from './components/jasmartyConfig/jasmartyConfig.component';
import { JasmartyFullViewComponent } from './components/jasmartyFullView/jasmartyFullView.component';
import { JasmartyMainComponent } from './components/jasmartyMain/jasmartyMain.component';
import { JasmartyPagesComponent, JasmartyPagesComponentGoToDialog } from './components/jasmartyPages/jasmartyPages.component';
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatBadgeModule } from '@angular/material/badge';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatNativeDateModule, MatRippleModule } from '@angular/material/core';
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
import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTreeModule } from '@angular/material/tree';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgModule } from '@angular/core';
import { NgxPhotoEditorModule } from 'ngx-photo-editor';
import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PortalModule } from '@angular/cdk/portal';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { SettingsComponent } from './components/settings/settings.component';
import { SidebarModule } from 'ng-sidebar';
import { TelegramChatComponent } from './components/telegramChat/telegramChat.component';
import { TelegramConfigComponent } from './components/telegramConfig/telegramConfig.component';
import { TelegramLogComponent } from './components/telegramLog/telegramLog.component';
import { TelegramMsgComponent, TelegramMsgComponentDialogComponent } from './components/telegramMsg/telegramMsg.component';
import { TodolistComponent, TodolistComponentDialogComponent } from './components/todolist/todolist.component';
import { WipfUserVwComponent, WipfUserVWComponentDialogComponent } from './components/wipfUserVw/wipfUserVw.component';

@NgModule({
  declarations: [
    AppComponent,
    CharGeneratorComponent,
    CryptComponent,
    DayLogComponent,
    DaylogComponentDialogDayComponent,
    DaylogComponentDialogEventComponent,
    DaylogComponentDialogTypeComponent,
    DaylogComponentDialogTypeListComponent,
    DaylogComponentEventlist,
    DebugSeiteComponent,
    DialogJaNeinComponent,
    DialogVariablenHilfeComponent,
    DialogWartenComponent,
    DynpagesComponent,
    DynpagesComponentDialogComponent,
    DynpageShowComponent,
    FileVwComponent,
    FilmeComponent,
    FilmeComponentDialog,
    FooterComponent,
    FooterComponentSetServerDialog,
    Jasmarty12864PagesComponent,
    Jasmarty12864PanelComponent,
    JasmartyActionsComponent,
    JasmartyActionsComponentDialog,
    JasmartyActionsComponentDialog,
    JasmartyConfigComponent,
    JasmartyFullViewComponent,
    JasmartyMainComponent,
    JasmartyPagesComponent,
    JasmartyPagesComponentGoToDialog,
    SettingsComponent,
    TelegramChatComponent,
    TelegramConfigComponent,
    TelegramLogComponent,
    TelegramMsgComponent,
    TelegramMsgComponentDialogComponent,
    TodolistComponent,
    TodolistComponentDialogComponent,
    WipfUserVwComponent,
    WipfUserVWComponentDialogComponent
  ],
  imports: [
    A11yModule,
    BrowserAnimationsModule,
    BrowserModule,
    CdkStepperModule,
    CdkTableModule,
    CdkTreeModule,
    ClipboardModule,
    DragDropModule,
    FormsModule,
    HttpClientModule,
    MatAutocompleteModule,
    MatBadgeModule,
    MatBottomSheetModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
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
    MatStepperModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    MatTreeModule,
    NgbModule,
    NgxPhotoEditorModule,
    PerfectScrollbarModule,
    PortalModule,
    ReactiveFormsModule,
    ScrollingModule,
    SidebarModule.forRoot(),
  ],
  providers: [{ provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } }],
  bootstrap: [AppComponent],
  entryComponents: [AppComponent],
})
export class AppModule { }
