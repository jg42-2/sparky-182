import axios from "axios";

export function getErrorMessage(err: unknown): string{

    if (axios.isAxiosError(err)){

        if(!err.response) return "No se pudo comunicar con el servidor (mn prendiste backend? o te olvidaste XD)";

        return err.response.data?.error ?? `Error: ${err.response.status}`;
    }

    return "Ocurrió un error inesperado, ga";


}

export function getFieldErrors(err: unknown): Record<string, string> {

    if (axios.isAxiosError(err)) return err.response?.data?.fields ?? {};
    return {};

}