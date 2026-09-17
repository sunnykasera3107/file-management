import api from "../api"

export async function uploadFile(
    FileList: React.ChangeEvent<HTMLInputElement>,
    onProgress: (progress: number) => void
) {
    if (!FileList.target.files || FileList.target.files == null || FileList.target.files.length <= 0) {
        return null;
    }
    const file = FileList.target.files[0];
    if (!file) return null;
    try{
        const formData = new FormData();
        formData.append("file", file);

        const response = await api.post("/api/v1/file", formData, 
            {
                timeout: 0,
                onUploadProgress: (progressEvent) => {
                    if (progressEvent.total) {
                        const progress = Math.round(
                            (progressEvent.loaded * 100) / progressEvent.total
                        );
                        onProgress(progress);
                    }
                },
            }
        );
        return response.data;
    } catch (e) {
        console.error(e);
    }
}

export async function listFiles<T>() {
    try{
        const response = await api.get("/api/v1/file/list");
        // console.log(response);
        return response.data as T;
    } catch (e) {
        console.error("Files load error", e);
    }
}

export async function getFile<T>(id: string) {
    try{
        const response = await api.get(`/api/v1/file/${id}`);
        return response.data as T;
    } catch (e) {
        console.error("File load error", e);
    }
}