import type { ReservationStatus } from './enums/ReservationStatus.tsx';

export interface Reservation {
  id: number;
  reservationTime: string; // ISO format: yyyy-MM-ddTHH:mm:ss
  numberOfGuests: number;
  notes?: string;
  status: ReservationStatus;
  createAt: string;

  customerName: string;
  tableName: string;
  branchName: string;
}
