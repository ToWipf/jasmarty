export interface jaconfig {
  port?: string;
  width?: number;
  height?: number;
  refreshrate?: number;
  baudrate?: number;
}

export interface japage {
  id?: number;
  name?: string;
  options?: string;
  lines?: string;
}

export interface buttonaction {
  id?: number;
  button?: number;
  active?: boolean;
  action?: string;
}
