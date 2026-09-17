import api from "../api"

export async function processFile( fileId: string ) {
    if (fileId == null) return null;
    try{
        const response = await api.post("/api/v1/file/process", null,
        {
            params: {
                fileId: fileId
            }
        });
        return response.data;
    } catch (e) {
        console.error(e);
    }
}