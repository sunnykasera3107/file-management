"use client";
import { Button } from "@/components/ui/button";
import File from "@/objects/File";
import { listFiles, uploadFile } from "@/services/files/manage";
import { RootState } from "@/store/store";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";

export default function Home() {
  const auth = useSelector((state: RootState) => state.auth);
  const [files, setFiles] = useState<File[]>([]);
  const [progress, setProgress] = useState<number>();


  const router = useRouter();


  useEffect(() => {
    if (!auth.isLoggedIn) {
      router.replace("/login");
    } else {
      const fetchListFiles = async () => {
        const response = await listFiles<File[]>();
        setFiles(response);
      }
      fetchListFiles();
    }
  }, [router, auth]);

  const handleUpload = async (element: React.ChangeEvent<HTMLInputElement>) => {
    await uploadFile(element, setProgress);
    const response = await listFiles<File[]>();
    setFiles(response);
  }

  const handleFileDelete = (id: string) =>{
    console.log("deleting" + id);
  }

  return (
    <div className="main bg-black h-[96vh]">
      <section className="mt-10 w-full max-w-4xl mx-auto text-right relative h-[20px]">
        <span className={"progress-bar h-[3px] w-[0%] bg-green-500 block self-start relative"} style={{width: `${progress}%`}}></span>
        <span className="group">
        <Button 
          className={"upload absolute right-0 z-0 text-md py-3 font-thin tracking-widest bg-gray-900 group-hover:bg-gray-600! border-0 rounded-none"}>
          Upload new file
        </Button>
        <input type="file" className="opacity-0 absolute right-0 z-3" onChange={((e) => handleUpload(e))}/>
        </span>
      </section>
      <section className="mt-15 w-full max-w-4xl mx-auto text-center relative">
        <div className="text-left w-full bold py-1 px-3 pb-5 flex flex-row">
          <div className="w-[70%]">File Name</div>
          <div className="w-[10%] text-right">Size</div>
          <div className="w-[20%] text-right">Actions</div>
        </div>
        <div>
          { Array.isArray(files) && files.map((file) => (
            <div key={file.filename} className="w-full py-1 px-3 flex flex-row odd:bg-gray-50/3">
              <div className="w-[70%] text-left">{file.filename}</div>
              <div className="w-[10%] text-right">{(file.size / 1024).toFixed(0)} KB</div>
              <div className="w-[20%] text-right">
                <div className="button-group">
                <Link 
                  className="button text-sm text-green-300 hover:border-b-1 pb-0" 
                  href={("/file/" + file.id)}
                >View file</Link>
                {/* <Button className={'delete cursor-pointer ml-3 text-sm font-normal text-red-700'} 
                    variant={"link"} 
                    onClick={() => {handleFileDelete(file.id as string)}}>Delete</Button> */}
                </div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
