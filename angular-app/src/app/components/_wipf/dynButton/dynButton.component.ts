import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ServiceRest } from 'src/app/service/serviceRest';
import { ServiceWipf } from 'src/app/service/serviceWipf';

@Component({
    selector: 'app-dynButton',
    templateUrl: './dynButton.component.html',
    styleUrls: ['./dynButton.component.less'],
    standalone: false
})
export class DynButtonComponent implements OnInit {

  constructor(private http: HttpClient, private rest: ServiceRest, public serviceWipf: ServiceWipf, public dialog: MatDialog) { }

  public buttodomain: string = "";
  public totpkey: string = "";
  private BUTTONDOMAINALIAS: string = "dynButtonDomain";
  private BUTTONTOTPKEY: string = "dynButtonTOTP";
  public nowTOTPkey: string = "";

  async ngOnInit(): Promise<void> {
    if (!localStorage.getItem(this.BUTTONDOMAINALIAS) || localStorage.getItem(this.BUTTONDOMAINALIAS) == "null") {
      // Initiale Config laden
      localStorage.setItem(this.BUTTONDOMAINALIAS, await this.rest.getConfigParam(this.BUTTONDOMAINALIAS));
    } else {
      this.buttodomain = localStorage.getItem(this.BUTTONDOMAINALIAS);
    }

    if (!localStorage.getItem(this.BUTTONTOTPKEY) || localStorage.getItem(this.BUTTONTOTPKEY) == "null") {
      // Initiale Config laden
      localStorage.setItem(this.BUTTONTOTPKEY, await this.rest.getConfigParam(this.BUTTONTOTPKEY));
    } else {
      this.totpkey = localStorage.getItem(this.BUTTONTOTPKEY);
    }
  }

  public doClickButton(): void {
    // Benutzung
    this.generateTOTP(this.totpkey)
      .then(totp => {
        console.log("Generated TOTP:", totp);
        this.nowTOTPkey = totp;

        let req = this.buttodomain + "?token=" + this.nowTOTPkey

        window.open(req);
        navigator.clipboard.writeText(req);

        this.http.get(req).subscribe((resdata: any) => {
          console.log(resdata);
        });
      })
      .catch(error => console.error("Error:", error));
  }

  private base32ToBytes(base32: string): Uint8Array {
    const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ234567';
    let bits = '';
    let bytes = [];

    base32 = base32.toUpperCase().replace(/=+$/, '');

    for (const char of base32) {
      const val = alphabet.indexOf(char);
      if (val === -1) continue;
      bits += val.toString(2).padStart(5, '0');
    }

    for (let i = 0; i + 8 <= bits.length; i += 8) {
      bytes.push(parseInt(bits.substring(i, i + 8), 2));
    }

    return new Uint8Array(bytes);
  }

  private async generateTOTP(secret: string, window: number = 30, digits: number = 6): Promise<string> {
    const key = this.base32ToBytes(secret);
    const counter = Math.floor(Date.now() / 1000 / window);

    const buffer = new ArrayBuffer(8);
    const view = new DataView(buffer);
    view.setUint32(4, counter); // Nur die letzten 4 Byte für 32-bit Counter (big endian)

    const cryptoKey = await crypto.subtle.importKey(
      'raw',
      key,
      { name: 'HMAC', hash: { name: 'SHA-1' } },
      false,
      ['sign']
    );

    const hmac = new Uint8Array(await crypto.subtle.sign('HMAC', cryptoKey, buffer));
    const offset = hmac[hmac.length - 1] & 0xf;
    const binary =
      ((hmac[offset] & 0x7f) << 24) |
      ((hmac[offset + 1] & 0xff) << 16) |
      ((hmac[offset + 2] & 0xff) << 8) |
      (hmac[offset + 3] & 0xff);

    const otp = binary % Math.pow(10, digits);
    return otp.toString().padStart(digits, '0');
  }

}
