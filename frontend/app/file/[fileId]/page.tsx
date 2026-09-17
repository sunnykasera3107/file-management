"use client";
import { useEffect, useState } from "react"
import File from "@/objects/File";
import { useParams } from "next/navigation";
import { processFile } from "@/services/files/analyse";
import { getFile } from "@/services/files/manage";
import { Button } from "@/components/ui/button";
import { useSelector } from "react-redux";
import { RootState } from "@/store/store";
import { useRouter } from "next/navigation";
import { average } from "@/components/lib/utils";
import Link from "next/link";

export default function ViewFile() {
    const params = useParams();
    const fileId = params.fileId as string;
    const [file, setFile] = useState<File>();
    const isLoggedIn = useSelector((state: RootState) => state.auth.isLoggedIn);
    const router = useRouter();
    const [count, setCount] = useState<Object>();
    const [process, setProcess] = useState<string>();

    
    useEffect(() => {
        if (!isLoggedIn) {
            router.replace("/login");
        }
        async function loadFile(fileId: string) {
            const response = await getFile(fileId);
            setFile(response);
            if (response.columnAnalysis) {
                console.log(response.columnAnalysis)
                const maxValue = Object.values(response.columnAnalysis).reduce(
                    (max, column) => Math.max(max, column.count),
                    0
                )
                setCount(maxValue);
            }
        }
        if (fileId && fileId != null) {
            loadFile(fileId);
        }
    }, [fileId, isLoggedIn, setFile]);

    const handleProcess = async () => {
        const response = await processFile(fileId);
        setProcess("In process");
        setFile(file);
        if (response.columnAnalysis) {
            const maxValue = Object.values(response.columnAnalysis).reduce(
                (max, column) => Math.max(max, column.count),
                0
            )
            setCount(maxValue);
        }
    }

    const loadAnalysis = async () => {
        const response = await getFile(fileId);
        setFile(response);
        if (response.columnAnalysis) {
            console.log(response.columnAnalysis)
            const maxValue = Object.values(response.columnAnalysis).reduce(
                (max, column) => Math.max(max, column.count),
                0
            )
            setCount(maxValue);
            setProcess(null);
        }
    }


    return (
        <div className="bg-black h-[96vh]">
            <section className="mt-15 w-full max-w-4xl mx-auto text-center relative">
                { file && (
                    <>
                    <div className="flex justify-between items-center">
                        <h1 className="text-3xl">{file.filename} <span className="text-sm! text-gray-400">of {average(file.size, 1024)} KB</span></h1>
                        { (!Object.hasOwn(file.metadata, "status")) && (
                            <div className="text-right group">
                                <Link href="/">Back to dashboard</Link>
                                { (file.metadata && "status" in file.metadata) ? 
                                    (<span className="text-green-700 ml-3">Process {file.metadata.status}</span> )
                                    : 
                                    ((process == null) ? 
                                        (<Button onClick={handleProcess} className="ml-3 bg-gray-700 border-0 rounded-none w-[60px]">Process</Button>)
                                        : 
                                        (<span className="ml-5 text-green-700 text-md">{process}</span>)
                                    )
                                }
                            </div>
                        )}
                        {
                            (count) && 
                            <div>
                                <Link href="/">Back to dashboard</Link>
                                <span className="text-green-700 ml-3">Total {count} Rows</span>
                            </div>
                        }
                    </div>
                    <div className="flex justify-center content-center flex-wrap mt-5">
                        {
                            (process && process != null) &&
                             (
                                <div className="h-[250px] flex justify-center items-center">
                                <Button onClick={loadAnalysis} className="ml-3 bg-gray-700/50 border-gray-300 hover:bg-gray-700/10 cursor-pointer border-1 rounded-none w-[100px] h-[100px] flex justify-center items-center rounded-full">
                                    Refresh
                                </Button>
                                </div>
                             )
                        }
                        {
                            (file.columnAnalysis) &&
                            
                            (Object.entries(file.columnAnalysis).map(([key, value]) => {
                                return (
                                    <div key={key} className="w-[200px] flex justify-center content-start min-h-[150px] flex-wrap mb-5 border-1 rounded-xl mx-2 bg-gray-900/50 p-3">
                                        <h3 className="text-[14px]">{value.columnName} <span className="text-[11px] text-yellow-300">({value.dataType} DATA)</span></h3>
                                        <div className="w-100 flex justify-around items-top py-3">
                                            <div>
                                                <div className="text-[12px]">Data Row Count</div>
                                                <div className="text-green-700 text-lg font-bold">{value.count}</div>
                                            </div>
                                            <div>
                                                <div className="text-[12px]">Null Row Count</div>
                                                <div className="text-red-700 text-lg font-bold">{value.nullCount}</div>
                                            </div>
                                        </div>
                                        { (value.dataType == "NUMERIC") &&
                                        <div className="w-[100%]">
                                            <div className="text-[10px] font-thin font-sans text-gray-50">
                                                <div>Minmum / Average / Maximum</div>
                                                <div className="text-[12px] font-normal">
                                                    <span className="text-yellow-500">{value.min.toFixed(0)}</span> / 
                                                    <span className="text-green-50"> {average(value.sum, (value.count - value.nullCount))}</span> / 
                                                    <span className="text-red-500"> {value.max.toFixed(0)}</span>
                                                </div>
                                            </div>
                                        </div>
                                        }
                                    </div>
                                )
                            }))
                        }
                    </div>
                    </>
                    )
                }
            </section>
        </div>
    )
}