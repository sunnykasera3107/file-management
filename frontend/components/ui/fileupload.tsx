"use client"

import { fileUpload } from "@/services/file_management/upload";
import { useState } from "react";
import { useSelector } from "react-redux";
import { RootState } from '@/store/store';
import { file } from "@/types/file";

interface FileUploadProps {
  setFiles: React.Dispatch<React.SetStateAction<file[]>>;
  files: file[];
}

export default function FileUpload({
  setFiles,
  files,
}: FileUploadProps) {
  const user = useSelector((state: RootState) => state.auth.user );
  const [progress, setProgress] = useState(0);
  const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!user || user.id == null) return;
    if (e.target.files &&  e.target.files?.length > 0) {
      const response = await fileUpload(e, user.id, setProgress);
      setFiles((files) => [
        ...files,
        response
      ]);
    }
  }
	return (
    <div className="flex justify-center align-center">
      <div className="w-[40%]">
        <form className="flex flex-col fileUpload w-[100%] h-[100px] mt-10 border-1 justify-center items-center text-center relative">
          <span className={"progress-bar h-[3px] bg-green-500 d-block self-start relative"} style={{width: `${progress}%`}}></span>
          <span className="absolute w-[100%] h-[100%] z-0 text-center flex justify-center items-center">Choose file to upload max 20 GB</span>
          <input type="file" className="file w-[100%] h-[100%] text-[0px] relative z-1" onChange={handleFileUpload}/>
        </form>
      </div>
    </div>
  );
}