import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatIconRegistry } from '@angular/material/icon';
import { MatDrawer } from '@angular/material/sidenav';
import { DomSanitizer } from '@angular/platform-browser';
import { Jaconfig } from './datatypes';
import { ServiceRest } from './service/serviceRest';

const ICON_BOX = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 5.75 3 A 1.0001 1.0001 0 0 0 4.8867188 3.4960938 L 3.1367188 6.4960938 A 1.0001 1.0001 0 0 0 3 7 L 3 19 C 3 20.093063 3.9069372 21 5 21 L 19 21 C 20.093063 21 21 20.093063 21 19 L 21 7 A 1.0001 1.0001 0 0 0 20.863281 6.4960938 L 19.113281 3.4960938 A 1.0001 1.0001 0 0 0 18.25 3 L 5.75 3 z M 6.3242188 5 L 17.675781 5 L 18.841797 7 L 5.1582031 7 L 6.3242188 5 z M 5 9 L 19 9 L 19 19 L 5 19 L 5 9 z M 9 11 L 9 13 L 15 13 L 15 11 L 9 11 z"/></svg>`;
const ICON_CANCEL = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 12 2 C 6.4889971 2 2 6.4889971 2 12 C 2 17.511003 6.4889971 22 12 22 C 17.511003 22 22 17.511003 22 12 C 22 6.4889971 17.511003 2 12 2 z M 12 4 C 16.430123 4 20 7.5698774 20 12 C 20 16.430123 16.430123 20 12 20 C 7.5698774 20 4 16.430123 4 12 C 4 7.5698774 7.5698774 4 12 4 z M 8.7070312 7.2929688 L 7.2929688 8.7070312 L 10.585938 12 L 7.2929688 15.292969 L 8.7070312 16.707031 L 12 13.414062 L 15.292969 16.707031 L 16.707031 15.292969 L 13.414062 12 L 16.707031 8.7070312 L 15.292969 7.2929688 L 12 10.585938 L 8.7070312 7.2929688 z"/></svg>`;
const ICON_CHECK_ALL = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 4 2 C 2.895 2 2 2.895 2 4 L 2 16 C 2 17.105 2.895 18 4 18 L 16 18 C 17.105 18 18 17.105 18 16 L 18 4 C 18 2.895 17.105 2 16 2 L 4 2 z M 4 4 L 16 4 L 16 16 L 4 16 L 4 4 z M 20 6 L 20 20 L 6 20 L 6 22 L 20 22 C 21.105 22 22 21.105 22 20 L 22 6 L 20 6 z M 13.292969 6.2929688 L 9 10.585938 L 6.7070312 8.2929688 L 5.2929688 9.7070312 L 9 13.414062 L 14.707031 7.7070312 L 13.292969 6.2929688 z"/></svg>`;
const ICON_CHECK_CIRCLE = `<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px">    <path d="M 12 2 C 6.486 2 2 6.486 2 12 C 2 17.514 6.486 22 12 22 C 17.514 22 22 17.514 22 12 C 22 10.874 21.803984 9.7942031 21.458984 8.7832031 L 19.839844 10.402344 C 19.944844 10.918344 20 11.453 20 12 C 20 16.411 16.411 20 12 20 C 7.589 20 4 16.411 4 12 C 4 7.589 7.589 4 12 4 C 13.633 4 15.151922 4.4938906 16.419922 5.3378906 L 17.851562 3.90625 C 16.203562 2.71225 14.185 2 12 2 z M 21.292969 3.2929688 L 11 13.585938 L 7.7070312 10.292969 L 6.2929688 11.707031 L 11 16.414062 L 22.707031 4.7070312 L 21.292969 3.2929688 z"/></svg>`;
const ICON_DOCUMENT = `<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px">    <path d="M 6 2 C 4.9057453 2 4 2.9057453 4 4 L 4 20 C 4 21.094255 4.9057453 22 6 22 L 18 22 C 19.094255 22 20 21.094255 20 20 L 20 8 L 14 2 L 6 2 z M 6 4 L 13 4 L 13 9 L 18 9 L 18 20 L 6 20 L 6 4 z M 8 12 L 8 14 L 16 14 L 16 12 L 8 12 z M 8 16 L 8 18 L 16 18 L 16 16 L 8 16 z"/></svg>`;
const ICON_EDIT = `<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px">    <path d="M 18.414062 2 C 18.158062 2 17.902031 2.0979687 17.707031 2.2929688 L 15.707031 4.2929688 L 14.292969 5.7070312 L 3 17 L 3 21 L 7 21 L 21.707031 6.2929688 C 22.098031 5.9019687 22.098031 5.2689063 21.707031 4.8789062 L 19.121094 2.2929688 C 18.926094 2.0979687 18.670063 2 18.414062 2 z M 18.414062 4.4140625 L 19.585938 5.5859375 L 18.292969 6.8789062 L 17.121094 5.7070312 L 18.414062 4.4140625 z M 15.707031 7.1210938 L 16.878906 8.2929688 L 6.171875 19 L 5 19 L 5 17.828125 L 15.707031 7.1210938 z"/></svg>`;
const ICON_EXTERN_LINK = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 5 3 C 3.9069372 3 3 3.9069372 3 5 L 3 19 C 3 20.093063 3.9069372 21 5 21 L 19 21 C 20.093063 21 21 20.093063 21 19 L 21 12 L 19 12 L 19 19 L 5 19 L 5 5 L 12 5 L 12 3 L 5 3 z M 14 3 L 14 5 L 17.585938 5 L 8.2929688 14.292969 L 9.7070312 15.707031 L 19 6.4140625 L 19 10 L 21 10 L 21 3 L 14 3 z"/></svg>`;
const ICON_HOME = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 12 2.0996094 L 1 12 L 4 12 L 4 21 L 11 21 L 11 15 L 13 15 L 13 21 L 20 21 L 20 12 L 23 12 L 12 2.0996094 z M 12 4.7910156 L 18 10.191406 L 18 11 L 18 19 L 15 19 L 15 13 L 9 13 L 9 19 L 6 19 L 6 10.191406 L 12 4.7910156 z"/></svg>`;
const ICON_INFO = `<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px">    <path d="M 12 2 C 6.4889971 2 2 6.4889971 2 12 C 2 17.511003 6.4889971 22 12 22 C 17.511003 22 22 17.511003 22 12 C 22 6.4889971 17.511003 2 12 2 z M 12 4 C 16.430123 4 20 7.5698774 20 12 C 20 16.430123 16.430123 20 12 20 C 7.5698774 20 4 16.430123 4 12 C 4 7.5698774 7.5698774 4 12 4 z M 11 7 L 11 9 L 13 9 L 13 7 L 11 7 z M 11 11 L 11 17 L 13 17 L 13 11 L 11 11 z"/></svg>`;
const ICON_OK = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 12 2 C 6.4889971 2 2 6.4889971 2 12 C 2 17.511003 6.4889971 22 12 22 C 17.511003 22 22 17.511003 22 12 C 22 6.4889971 17.511003 2 12 2 z M 12 4 C 16.430123 4 20 7.5698774 20 12 C 20 16.430123 16.430123 20 12 20 C 7.5698774 20 4 16.430123 4 12 C 4 7.5698774 7.5698774 4 12 4 z M 16.292969 8.2929688 L 10 14.585938 L 7.7070312 12.292969 L 6.2929688 13.707031 L 10 17.414062 L 17.707031 9.7070312 L 16.292969 8.2929688 z"/></svg>`;
const ICON_PICTURE = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 4 4 C 2.9069372 4 2 4.9069372 2 6 L 2 18 C 2 19.093063 2.9069372 20 4 20 L 20 20 C 21.093063 20 22 19.093063 22 18 L 22 6 C 22 4.9069372 21.093063 4 20 4 L 4 4 z M 4 6 L 20 6 L 20 18 L 4 18 L 4 6 z M 14.5 11 L 11 15 L 8.5 12.5 L 5.7773438 16 L 18.25 16 L 14.5 11 z"/></svg>`;
const ICON_PUZZLE = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 11 1 C 9.3550302 1 8 2.3550302 8 4 L 4 4 C 2.9069372 4 2 4.9069372 2 6 L 2 12 L 4 12 C 4.5650302 12 5 12.43497 5 13 C 5 13.56503 4.5650302 14 4 14 L 2 14 L 2 20 C 2 21.093063 2.9069372 22 4 22 L 10 22 L 10 20 C 10 19.43497 10.43497 19 11 19 C 11.56503 19 12 19.43497 12 20 L 12 22 L 18 22 C 19.093063 22 20 21.093063 20 20 L 20 16 C 21.64497 16 23 14.64497 23 13 C 23 11.35503 21.64497 10 20 10 L 20 6 C 20 4.9069372 19.093063 4 18 4 L 14 4 C 14 2.3550302 12.64497 1 11 1 z M 11 3 C 11.56503 3 12 3.4349698 12 4 L 12 6 L 18 6 L 18 12 L 20 12 C 20.56503 12 21 12.43497 21 13 C 21 13.56503 20.56503 14 20 14 L 18 14 L 18 20 L 14 20 C 14 18.35503 12.64497 17 11 17 C 9.3550302 17 8 18.35503 8 20 L 4 20 L 4 16 C 5.6449698 16 7 14.64497 7 13 C 7 11.35503 5.6449698 10 4 10 L 4 6 L 10 6 L 10 4 C 10 3.4349698 10.43497 3 11 3 z"/></svg>`;
const ICON_RESTART = `<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px">    <path d="M 2 2 L 4.9394531 4.9394531 C 3.1262684 6.7482143 2 9.2427079 2 12 C 2 17.514 6.486 22 12 22 C 17.514 22 22 17.514 22 12 C 22 6.486 17.514 2 12 2 L 12 4 C 16.411 4 20 7.589 20 12 C 20 16.411 16.411 20 12 20 C 7.589 20 4 16.411 4 12 C 4 9.7940092 4.9004767 7.7972757 6.3496094 6.3496094 L 9 9 L 9 2 L 2 2 z"/></svg>`;
const ICON_ROUND = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 12 2 C 6.4889971 2 2 6.4889971 2 12 C 2 17.511003 6.4889971 22 12 22 C 17.511003 22 22 17.511003 22 12 C 22 6.4889971 17.511003 2 12 2 z M 12 4 C 16.430123 4 20 7.5698774 20 12 C 20 16.430123 16.430123 20 12 20 C 7.5698774 20 4 16.430123 4 12 C 4 7.5698774 7.5698774 4 12 4 z"/></svg>`;
const ICON_SEARCH = `<?xml version="1.0"?><svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px">    <path d="M 9 2 C 5.1458514 2 2 5.1458514 2 9 C 2 12.854149 5.1458514 16 9 16 C 10.747998 16 12.345009 15.348024 13.574219 14.28125 L 14 14.707031 L 14 16 L 20 22 L 22 20 L 16 14 L 14.707031 14 L 14.28125 13.574219 C 15.348024 12.345009 16 10.747998 16 9 C 16 5.1458514 12.854149 2 9 2 z M 9 4 C 11.773268 4 14 6.2267316 14 9 C 14 11.773268 11.773268 14 9 14 C 6.2267316 14 4 11.773268 4 9 C 4 6.2267316 6.2267316 4 9 4 z"/></svg>`;
const ICON_SERVICES = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 16 2 L 16 3.203125 C 15.257598 3.384199 14.599699 3.6951502 14.125 4.1855469 L 13.169922 3.6347656 L 12.169922 5.3652344 L 13.177734 5.9472656 C 13.090179 6.2951157 13 6.6445314 13 7 C 13 7.3554686 13.09018 7.7048843 13.177734 8.0527344 L 12.169922 8.6347656 L 13.169922 10.365234 L 14.125 9.8144531 C 14.599699 10.30485 15.257598 10.615801 16 10.796875 L 16 12 L 18 12 L 18 10.796875 C 18.742402 10.615801 19.400301 10.30485 19.875 9.8144531 L 20.830078 10.365234 L 21.830078 8.6347656 L 20.822266 8.0527344 C 20.909821 7.7048843 21 7.3554686 21 7 C 21 6.6445314 20.909821 6.2951157 20.822266 5.9472656 L 21.830078 5.3652344 L 20.830078 3.6347656 L 19.875 4.1855469 C 19.400301 3.6951502 18.742402 3.384199 18 3.203125 L 18 2 L 16 2 z M 17 5 C 17.749999 5 18.185226 5.2452444 18.501953 5.6015625 C 18.81868 5.9578806 19 6.4722221 19 7 C 19 7.5277779 18.81868 8.0421194 18.501953 8.3984375 C 18.185226 8.7547556 17.749999 9 17 9 C 16.250001 9 15.814774 8.7547556 15.498047 8.3984375 C 15.18132 8.0421194 15 7.5277779 15 7 C 15 6.4722221 15.18132 5.9578806 15.498047 5.6015625 C 15.814774 5.2452444 16.250001 5 17 5 z M 7 10 L 7 11.201172 C 5.8893376 11.413372 4.904113 11.852257 4.2519531 12.585938 C 4.2327931 12.607487 4.2276904 12.636373 4.2089844 12.658203 L 3.3046875 12.134766 L 2.3046875 13.865234 L 3.2675781 14.421875 C 3.1102522 14.936467 3 15.466202 3 16 C 3 16.533798 3.1102519 17.063538 3.2675781 17.578125 L 2.3046875 18.134766 L 3.3046875 19.865234 L 4.2089844 19.341797 C 4.2276904 19.363637 4.2327931 19.392502 4.2519531 19.414062 C 4.9041127 20.147739 5.8893376 20.586628 7 20.798828 L 7 22 L 9 22 L 9 20.798828 C 10.110662 20.586628 11.095887 20.147744 11.748047 19.414062 C 11.767207 19.392512 11.772306 19.363637 11.791016 19.341797 L 12.695312 19.865234 L 13.695312 18.134766 L 12.732422 17.578125 C 12.889747 17.063543 13 16.533798 13 16 C 13 15.466202 12.889748 14.936462 12.732422 14.421875 L 13.695312 13.865234 L 12.695312 12.134766 L 11.791016 12.658203 C 11.772306 12.636363 11.767207 12.607487 11.748047 12.585938 C 11.095888 11.852252 10.110662 11.413372 9 11.201172 L 9 10 L 7 10 z M 8 13 C 9.0833326 13 9.7685596 13.370244 10.251953 13.914062 C 10.735347 14.457882 11 15.222222 11 16 C 11 16.777778 10.735347 17.542119 10.251953 18.085938 C 9.7685596 18.629756 9.0833326 19 8 19 C 6.9166674 19 6.2314405 18.629756 5.7480469 18.085938 C 5.2646532 17.542119 5 16.777778 5 16 C 5 15.222222 5.2646532 14.457881 5.7480469 13.914062 C 6.2314405 13.370245 6.9166674 13 8 13 z M 8 15 A 1 1 0 0 0 8 17 A 1 1 0 0 0 8 15 z"/></svg>`;
const ICON_SETTINGS = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 9.6660156 2 L 9.1757812 4.5234375 C 8.3516137 4.8342536 7.5947862 5.2699307 6.9316406 5.8144531 L 4.5078125 4.9785156 L 2.171875 9.0214844 L 4.1132812 10.708984 C 4.0386488 11.16721 4 11.591845 4 12 C 4 12.408768 4.0398071 12.832626 4.1132812 13.291016 L 4.1132812 13.292969 L 2.171875 14.980469 L 4.5078125 19.021484 L 6.9296875 18.1875 C 7.5928951 18.732319 8.3514346 19.165567 9.1757812 19.476562 L 9.6660156 22 L 14.333984 22 L 14.824219 19.476562 C 15.648925 19.165543 16.404903 18.73057 17.068359 18.185547 L 19.492188 19.021484 L 21.826172 14.980469 L 19.886719 13.291016 C 19.961351 12.83279 20 12.408155 20 12 C 20 11.592457 19.96113 11.168374 19.886719 10.710938 L 19.886719 10.708984 L 21.828125 9.0195312 L 19.492188 4.9785156 L 17.070312 5.8125 C 16.407106 5.2676813 15.648565 4.8344327 14.824219 4.5234375 L 14.333984 2 L 9.6660156 2 z M 11.314453 4 L 12.685547 4 L 13.074219 6 L 14.117188 6.3945312 C 14.745852 6.63147 15.310672 6.9567546 15.800781 7.359375 L 16.664062 8.0664062 L 18.585938 7.40625 L 19.271484 8.5917969 L 17.736328 9.9277344 L 17.912109 11.027344 L 17.912109 11.029297 C 17.973258 11.404235 18 11.718768 18 12 C 18 12.281232 17.973259 12.595718 17.912109 12.970703 L 17.734375 14.070312 L 19.269531 15.40625 L 18.583984 16.59375 L 16.664062 15.931641 L 15.798828 16.640625 C 15.308719 17.043245 14.745852 17.36853 14.117188 17.605469 L 14.115234 17.605469 L 13.072266 18 L 12.683594 20 L 11.314453 20 L 10.925781 18 L 9.8828125 17.605469 C 9.2541467 17.36853 8.6893282 17.043245 8.1992188 16.640625 L 7.3359375 15.933594 L 5.4140625 16.59375 L 4.7285156 15.408203 L 6.265625 14.070312 L 6.0878906 12.974609 L 6.0878906 12.972656 C 6.0276183 12.596088 6 12.280673 6 12 C 6 11.718768 6.026742 11.404282 6.0878906 11.029297 L 6.265625 9.9296875 L 4.7285156 8.59375 L 5.4140625 7.40625 L 7.3359375 8.0683594 L 8.1992188 7.359375 C 8.6893282 6.9567546 9.2541467 6.6314701 9.8828125 6.3945312 L 10.925781 6 L 11.314453 4 z M 12 8 C 9.8034768 8 8 9.8034768 8 12 C 8 14.196523 9.8034768 16 12 16 C 14.196523 16 16 14.196523 16 12 C 16 9.8034768 14.196523 8 12 8 z M 12 10 C 13.111477 10 14 10.888523 14 12 C 14 13.111477 13.111477 14 12 14 C 10.888523 14 10 13.111477 10 12 C 10 10.888523 10.888523 10 12 10 z"/></svg>`;
const ICON_SUPPORT = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 7.5 1 C 6.6274862 1 5.7932031 1.1755485 5.0332031 1.4882812 L 3.6445312 2.0585938 L 7.5859375 6 L 6 7.5859375 L 2.0585938 3.6445312 L 1.4882812 5.0332031 C 1.1755484 5.7932031 1 6.6274862 1 7.5 C 1 11.078268 3.9217323 14 7.5 14 C 8.11867 14 8.6809561 13.816598 9.2480469 13.654297 L 18.15625 22.560547 C 18.734417 23.138714 19.699176 23.138714 20.277344 22.560547 L 22.5625 20.273438 C 23.136532 19.696462 23.137552 18.731255 22.560547 18.154297 L 13.654297 9.2480469 C 13.816352 8.6810027 14 8.1186541 14 7.5 C 14 3.9217323 11.078268 1 7.5 1 z M 7.5 3 C 9.9977323 3 12 5.0022677 12 7.5 C 12 8.1069469 11.878016 8.6818072 11.660156 9.2128906 L 11.408203 9.8300781 L 20.792969 19.214844 L 19.216797 20.792969 L 9.8300781 11.40625 L 9.2128906 11.660156 C 8.6825114 11.878548 8.1081276 12 7.5 12 C 5.0022677 12 3 9.9977323 3 7.5 C 3 7.47441 3.0132432 7.4532073 3.0136719 7.4277344 L 6 10.414062 L 10.414062 6 L 7.4277344 3.0136719 C 7.4532073 3.0132432 7.47441 3 7.5 3 z"/></svg>`;
const ICON_THUMBUP = `<svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px"><path d="M0 0h24v24H0z" fill="none"/><path d="M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.` + `44-1.06L14.17 1 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5` + `1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-1.91l-.01-.01L23 10z"/></svg>`;
const ICON_TRASH = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 10 2 L 9 3 L 4 3 L 4 5 L 5 5 L 5 20 C 5 20.522222 5.1913289 21.05461 5.5683594 21.431641 C 5.9453899 21.808671 6.4777778 22 7 22 L 17 22 C 17.522222 22 18.05461 21.808671 18.431641 21.431641 C 18.808671 21.05461 19 20.522222 19 20 L 19 5 L 20 5 L 20 3 L 15 3 L 14 2 L 10 2 z M 7 5 L 17 5 L 17 20 L 7 20 L 7 5 z M 9 7 L 9 18 L 11 18 L 11 7 L 9 7 z M 13 7 L 13 18 L 15 18 L 15 7 L 13 7 z"/></svg>`;
const ICON_UNAVAILABLE = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 12 2 C 6.4889971 2 2 6.4889971 2 12 C 2 17.511003 6.4889971 22 12 22 C 17.511003 22 22 17.511003 22 12 C 22 6.4889971 17.511003 2 12 2 z M 12 4 C 16.430123 4 20 7.5698774 20 12 C 20 13.85307 19.369262 15.55056 18.318359 16.904297 L 7.0957031 5.6816406 C 8.4494397 4.6307377 10.14693 4 12 4 z M 5.6816406 7.0957031 L 16.904297 18.318359 C 15.55056 19.369262 13.85307 20 12 20 C 7.5698774 20 4 16.430123 4 12 C 4 10.14693 4.6307377 8.4494397 5.6816406 7.0957031 z"/></svg>`;
const ICON_USER = `<svg xmlns="http://www.w3.org/2000/svg"  viewBox="0 0 24 24" width="24px" height="24px"><path d="M 12 2 C 8 2 2 5 2 20 L 6.7285156 20 C 8.1385798 21.240809 9.9815281 22 12 22 C 14.018472 22 15.86142 21.240809 17.271484 20 L 22 20 C 22 5 16 2 12 2 z M 12 4 C 13.669 4 16.871734 4.75025 18.677734 10.03125 C 17.967734 9.39025 17.029 9 16 9 L 8 9 C 6.971 9 6.0322656 9.39025 5.3222656 10.03125 C 7.1282656 4.75025 10.331 4 12 4 z M 8 11 L 16 11 C 17.105 11 18 11.895 18 13 L 18 14 C 18 17.325562 15.325562 20 12 20 C 8.674438 20 6 17.325562 6 14 L 6 13 C 6 11.895 6.895 11 8 11 z"/></svg>`;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
})
export class AppComponent implements OnInit {
  constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer, private http: HttpClient, private rest: ServiceRest) {

    iconRegistry.addSvgIconLiteral('box', sanitizer.bypassSecurityTrustHtml(ICON_BOX));
    iconRegistry.addSvgIconLiteral('cancel', sanitizer.bypassSecurityTrustHtml(ICON_CANCEL));
    iconRegistry.addSvgIconLiteral('check_all', sanitizer.bypassSecurityTrustHtml(ICON_CHECK_ALL));
    iconRegistry.addSvgIconLiteral('check_circle', sanitizer.bypassSecurityTrustHtml(ICON_CHECK_CIRCLE));
    iconRegistry.addSvgIconLiteral('document', sanitizer.bypassSecurityTrustHtml(ICON_DOCUMENT));
    iconRegistry.addSvgIconLiteral('edit', sanitizer.bypassSecurityTrustHtml(ICON_EDIT));
    iconRegistry.addSvgIconLiteral('extern_link', sanitizer.bypassSecurityTrustHtml(ICON_EXTERN_LINK));
    iconRegistry.addSvgIconLiteral('home', sanitizer.bypassSecurityTrustHtml(ICON_HOME));
    iconRegistry.addSvgIconLiteral('info', sanitizer.bypassSecurityTrustHtml(ICON_INFO));
    iconRegistry.addSvgIconLiteral('ok', sanitizer.bypassSecurityTrustHtml(ICON_OK));
    iconRegistry.addSvgIconLiteral('picture', sanitizer.bypassSecurityTrustHtml(ICON_PICTURE));
    iconRegistry.addSvgIconLiteral('puzzle', sanitizer.bypassSecurityTrustHtml(ICON_PUZZLE));
    iconRegistry.addSvgIconLiteral('restart', sanitizer.bypassSecurityTrustHtml(ICON_RESTART));
    iconRegistry.addSvgIconLiteral('round', sanitizer.bypassSecurityTrustHtml(ICON_ROUND));
    iconRegistry.addSvgIconLiteral('search', sanitizer.bypassSecurityTrustHtml(ICON_SEARCH));
    iconRegistry.addSvgIconLiteral('services', sanitizer.bypassSecurityTrustHtml(ICON_SERVICES));
    iconRegistry.addSvgIconLiteral('settings', sanitizer.bypassSecurityTrustHtml(ICON_SETTINGS));
    iconRegistry.addSvgIconLiteral('support', sanitizer.bypassSecurityTrustHtml(ICON_SUPPORT));
    iconRegistry.addSvgIconLiteral('thumbsup', sanitizer.bypassSecurityTrustHtml(ICON_THUMBUP));
    iconRegistry.addSvgIconLiteral('trash', sanitizer.bypassSecurityTrustHtml(ICON_TRASH));
    iconRegistry.addSvgIconLiteral('unavailable', sanitizer.bypassSecurityTrustHtml(ICON_UNAVAILABLE));
    iconRegistry.addSvgIconLiteral('user', sanitizer.bypassSecurityTrustHtml(ICON_USER));
  }

  @ViewChild(MatDrawer, { static: true }) drawer: MatDrawer;

  public bTelegramActive: boolean = false;
  public bJasmartyActive: boolean = false;
  public bDevActive: boolean = false;
  public bWipfActive: boolean = false;
  public jasmartyType: string;
  public selectedSite: string = 'firstpage';

  ngOnInit(): void {
    this.rest.sethostExpect();
    this.getActiveModules();
  }

  public selectSite(s: string) {
    this.drawer.toggle();
    this.selectedSite = s;
  }

  public getActiveModules(): void {
    this.bDevActive = false;

    this.http.get(this.rest.gethost() + 'basesettings/get/wipf').subscribe((resdata: any) => {
      this.bWipfActive = resdata.active;
    });

    this.http.get(this.rest.gethost() + 'basesettings/get/telegram').subscribe((resdata: any) => {
      this.bTelegramActive = resdata.active;
    });

    this.http.get(this.rest.gethost() + 'basesettings/get/jasmarty').subscribe((resdata: any) => {
      this.bJasmartyActive = resdata.active;

      if (this.bJasmartyActive) {
        // Wenn Jasmarty true ist, den Typ holen
        this.http.get(this.rest.gethost() + 'lcd/config/get').subscribe((resdata) => {
          var jaconfig: Jaconfig = resdata;
          this.jasmartyType = jaconfig.type;
        });
      }
    });
    this.http.get(this.rest.gethost() + 'basesettings/get/debug').subscribe((resdata: any) => {
      this.bDevActive = resdata.active;
    });

  }

  public showDevModules(): void {
    this.bDevActive = !this.bDevActive;
  }

  public showAll2004(): void {
    this.bJasmartyActive = true;
    this.bTelegramActive = true;
    this.bWipfActive = true;
    this.jasmartyType = "LCD_2004";
  }

  public showAll12864(): void {
    this.bJasmartyActive = true;
    this.bTelegramActive = true;
    this.bWipfActive = true;
    this.jasmartyType = "LCD_12864";
  }
}
