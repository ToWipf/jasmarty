import { A11yModule } from '@angular/cdk/a11y';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AuthKeyComponent, AuthKeyComponentDialogComponent } from './components/_main/authKey/authKey.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { ChecklisteComponent, CheckListeDialogCheckListe, CheckListeDialogItem, CheckListeDialogType } from './components/_wipf/checkliste/checkliste.component';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { ColorPickerModule } from 'ngx-color-picker';
import { CookieAddDialogComponent, CookieDialogComponent } from './dialog/cookieVW/cookie.dialog';
import { CryptComponent } from './components/_debug/crypt/crypt.component';
import { DayLogComponent, DaylogComponentDialogDayComponent, DaylogComponentDialogTypeComponent, DaylogComponentDialogTypeListComponent } from './components/_wipf/daylog/daylog.component';
import { DaylogComponentEventlist, DaylogComponentDialogEventComponent } from './components/_wipf/daylog/daylog.eventlist';
import { DaylogKalenderComponent } from './components/_wipf/daylogKalender/daylogKalender.component';
import { DaylogStatsComponent } from './components/_wipf/daylogStats/daylogStats.component';
import { DebugSeiteComponent } from './components/_debug/debugSeite/debugSeite.component';
import { DialogInfoboxComponent, DialogInputOneThingComponent, DialogJaNeinComponent, DialogVariablenHilfeComponent, DialogWartenComponent } from './dialog/main.dialog';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { EisenbahnMitlesenComponent } from './components/_eisenbahn/mitlesen/eisenbahn-mitlesen.component';
import { ElementSetServerDialog } from './dialog/setServer.dialog';
import { FileVwComponent } from './components/_main/fileVw/fileVw.component';
import { FooterComponent } from './components/_main/footer/footer.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GlowiComponent } from './components/glowi/glowi.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Jasmarty12864PagesComponent } from './components/_jasmarty/jasmarty12864Pages/jasmarty12864Pages.component';
import { Jasmarty12864PanelComponent } from './components/_jasmarty/jasmarty12864Panel/jasmarty12864Panel.component';
import { JasmartyActionsComponent, JasmartyActionsComponentDialog } from './components/_jasmarty/jasmartyActions/jasmartyActions.component';
import { JasmartyConfigComponent } from './components/_jasmarty/jasmartyConfig/jasmartyConfig.component';
import { ListeComponent, ListeComponentDialogComponent } from './components/_wipf/liste/liste.component';
import { ListeCryptComponentDialogComponent } from './components/_wipf/liste/listeCrypt.component';
import { ListeTypeComponentDialogTypeComponent, ListeTypeComponentDialogTypeListComponent } from './components/_wipf/liste/listeType.component';
import { LocalStorageVWAddDialogComponent, LocalStorageDialogComponent } from './dialog/localStorageVW/localStorageVW.dialog';
import { LoginComponent } from './components/_main/login/login.component';
import { MainmenueComponent } from './components/_main/mainmenue/mainmenue.component';
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
import { MedienComponent, MedienComponentDialog } from './components/_wipf/medien/medien.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgModule, isDevMode } from '@angular/core';
import { NgScrollbarModule } from 'ngx-scrollbar';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { NgxPhotoEditorModule } from 'ngx-photo-editor';
import { NgxView360Module } from "@egjs/ngx-view360";
import { PortalModule } from '@angular/cdk/portal';
import { RndEventComponent, RndEventComponentDialogComponent } from './components/_wipf/rndEvent/rndEvent.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { ServiceWorkerModule } from '@angular/service-worker';
import { SettingsComponent, SettingsComponentDialogComponent } from './components/_main/settings/settings.component';
import { TelegramChatComponent } from './components/_telegram/telegramChat/telegramChat.component';
import { TelegramConfigComponent } from './components/_telegram/telegramConfig/telegramConfig.component';
import { TelegramLogComponent } from './components/_telegram/telegramLog/telegramLog.component';
import { TelegramMsgComponent, TelegramMsgComponentDialogComponent } from './components/_telegram/telegramMsg/telegramMsg.component';
import { View360Component } from './components/_debug/view360/view360.component';
import { WipfUserVwComponent, WipfUserVWComponentDialogComponent } from './components/_main/wipfUserVw/wipfUserVw.component';
import { DynButtonComponent } from './components/_wipf/dynButton/dynButton.component';

@NgModule({
    declarations: [
        AppComponent,
        AuthKeyComponent,
        AuthKeyComponentDialogComponent,
        ChecklisteComponent,
        CheckListeDialogCheckListe,
        CheckListeDialogItem,
        CheckListeDialogType,
        CookieAddDialogComponent,
        CookieDialogComponent,
        CryptComponent,
        DayLogComponent,
        DaylogComponentDialogDayComponent,
        DaylogComponentDialogEventComponent,
        DaylogComponentDialogTypeComponent,
        DaylogComponentDialogTypeListComponent,
        DaylogComponentEventlist,
        DaylogKalenderComponent,
        DaylogStatsComponent,
        DebugSeiteComponent,
        DialogInputOneThingComponent,
        DialogInfoboxComponent,
        DialogJaNeinComponent,
        DialogVariablenHilfeComponent,
        DialogWartenComponent,
        DynButtonComponent,
        EisenbahnMitlesenComponent,
        ElementSetServerDialog,
        FileVwComponent,
        FooterComponent,
        GlowiComponent,
        Jasmarty12864PagesComponent,
        Jasmarty12864PanelComponent,
        JasmartyActionsComponent,
        JasmartyActionsComponentDialog,
        JasmartyActionsComponentDialog,
        JasmartyConfigComponent,
        ListeComponent,
        ListeComponentDialogComponent,
        ListeCryptComponentDialogComponent,
        ListeTypeComponentDialogTypeComponent,
        ListeTypeComponentDialogTypeListComponent,
        LocalStorageVWAddDialogComponent,
        LocalStorageDialogComponent,
        LoginComponent,
        MainmenueComponent,
        MedienComponent,
        MedienComponentDialog,
        RndEventComponent,
        RndEventComponentDialogComponent,
        SettingsComponent,
        SettingsComponentDialogComponent,
        TelegramChatComponent,
        TelegramConfigComponent,
        TelegramLogComponent,
        TelegramMsgComponent,
        TelegramMsgComponentDialogComponent,
        View360Component,
        WipfUserVwComponent,
        WipfUserVWComponentDialogComponent
    ],
    imports: [
        A11yModule, //TODO:?
        AppRoutingModule,
        BrowserAnimationsModule,
        BrowserModule,
        BrowserModule,
        CdkStepperModule,
        CdkTableModule,
        CdkTreeModule,
        ClipboardModule,
        ColorPickerModule,
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
        NgScrollbarModule,
        NgxChartsModule,
        NgxPhotoEditorModule,
        NgxPhotoEditorModule,
        NgxView360Module,
        PortalModule,
        ReactiveFormsModule,
        ScrollingModule,
        ServiceWorkerModule.register('ngsw-worker.js', {
          enabled: !isDevMode(),
          // Register the ServiceWorker as soon as the application is stable
          // or after 30 seconds (whichever comes first).
          registrationStrategy: 'registerWhenStable:30000'
        }),
    ],
    providers: [
        { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } },
        HttpClient,
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
