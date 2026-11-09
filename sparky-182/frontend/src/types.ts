//auth
export interface RegisterRequest {
    email: string;
    password: string;
    name: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface AuthResponse{
    token: string;
}

// albumes y canciones
export interface Album{
    id: number;
    title: string;
    year: number;
    coverUrl: string;
    lineup: string;
}

export interface AlbumDetail extends Album{
    songCount: number;
    averageRating: number;
    reviewCount: number;
}

export interface Song{
    id: number;
    title: string;
    duration: string;
    trackNumber: number;
    albumId: number;
    albumTitle: string;
    
}
export interface FavoriteRequest{
    songId: number;
}

export type ListenStatus = "escuchando" | "escuchado";

export interface HistoryRequest{
    albumId: number;
    status: ListenStatus;
}

export interface AlbumIdRequest{
    albumId: number;
}

export interface HistoryEntry{
    album: Album;
    status: ListenStatus;
}

export interface Review{
    id: number;
    albumId: number;
    rating: number;
    comment: string;
    authorName: string;
    authorEmail: string;
    createdAt: string;
}

export interface ReviewRequest{
    rating: number;
    comment: string;
}

export interface Page<T>{
    content: T[];
    number: number;
    size: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
}

export interface Tour{
    id: number;
    city: string;
    country: string;
    venue: string;
    date: string;
    tickesAvailable: number;
    price: number;
    soldOut: boolean;
}


export interface TicketResponse{
    tourId: number;
    city: string;
    quantity: number;
    remaining: number;
}

export interface TourRequest{
    city: string;
    country: string;
    venue: string;
    date: string;
    ticketsAvailable: number;
    price: number;
}

export interface Me{
    id: number;
    name: string;
    email: string;
    role: "USER" | "ADMIN";
}