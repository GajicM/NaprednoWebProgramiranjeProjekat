// Purpose: User data transfer object for the user service.
export interface User {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    token?: string;
    permissions: string[];
}
