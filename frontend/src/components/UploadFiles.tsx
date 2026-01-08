import React, { useMemo, useRef, useState } from "react";
import { Upload, FileText, X, AlertTriangle } from "lucide-react";

type PickedFile = {
    file: File;
    id: string;
};

function formatBytes(bytes: number) {
    if (!bytes) return "0 B";
    const k = 1024;
    const sizes = ["B", "KB", "MB", "GB"];
    const i = Math.min(Math.floor(Math.log(bytes) / Math.log(k)), sizes.length - 1);
    return `${(bytes / Math.pow(k, i)).toFixed(i === 0 ? 0 : 1)} ${sizes[i]}`;
}

function dedupId(f: File) {
    return `${f.name}__${f.size}__${f.lastModified}`;
}

const uploadContainer = "w-full";

export function UploadFiles() {
    const inputRef = useRef<HTMLInputElement | null>(null);
    const [dragActive, setDragActive] = useState(false);
    const [items, setItems] = useState<PickedFile[]>([]);
    const [error, setError] = useState<string | null>(null);

    const maxFiles = 5;
    const maxSizeBytes = 10 * 1024 * 1024;
    const accept = useMemo(() => ".pdf,application/pdf", []);

    function mergeFiles(incoming: File[]) {
        const currentMap = new Map(items.map((it) => [it.id, it]));
        for (const f of incoming) {
            const id = dedupId(f);
            currentMap.set(id, { id, file: f });
        }

        const merged = Array.from(currentMap.values());
        if (merged.length > maxFiles) {
            setError(`Too many files. Max ${maxFiles}.`);
            setItems(merged.slice(0, maxFiles));
            return;
        }

        setItems(merged);
    }

    function validateAndAdd(incoming: File[]) {
        if (incoming.length === 0) return;

        const onlyPdf = incoming.filter(
            (f) => f.type === "application/pdf" || f.name.toLowerCase().endsWith(".pdf")
        );
        const hadInvalidType = onlyPdf.length !== incoming.length;

        const sizeOk = onlyPdf.filter((f) => f.size <= maxSizeBytes);
        const hadTooLarge = sizeOk.length !== onlyPdf.length;

        if (hadInvalidType) {
            setError("Invalid file type. Only PDF files are allowed.");
        } else if (hadTooLarge) {
            setError(`Some files are too large. Max ${formatBytes(maxSizeBytes)} each.`);
        } else {
            setError(null);
        }

        if (sizeOk.length > 0) mergeFiles(sizeOk);
    }

    function onPick(e: React.ChangeEvent<HTMLInputElement>) {
        const incoming = Array.from(e.target.files ?? []);
        validateAndAdd(incoming);
        e.target.value = "";
    }

    function onDrop(e: React.DragEvent<HTMLDivElement>) {
        e.preventDefault();
        e.stopPropagation();
        setDragActive(false);
        const incoming = Array.from(e.dataTransfer.files ?? []);
        validateAndAdd(incoming);
    }

    function removeById(id: string) {
        setItems((prev) => prev.filter((it) => it.id !== id));
        setError(null);
    }

    const dropzone =
        "relative w-full rounded-[var(--radius-xl)] border-2 border-dashed px-6 py-6 transition-all select-none " +
        (dragActive
            ? "border-[var(--primary-color)] bg-[color:rgba(37,99,235,0.10)] scale-[1.01)]"
            : "border-[var(--gray-300)] bg-[var(--background-light)] hover:border-[var(--primary-color)] hover:bg-[color:rgba(37,99,235,0.06)]") +
        " shadow-[var(--shadow-sm)] cursor-pointer";

    const helperText = "text-sm text-[var(--text-light)]";
    const titleText = "text-base font-semibold text-[var(--text-color)]";
    const chip =
        "inline-flex items-center gap-2 rounded-lg border border-[var(--gray-200)] bg-[var(--white)] px-3 py-1.5 text-sm shadow-[var(--shadow-sm)]";

    return (
        <div className={uploadContainer}>
            <div
                className={dropzone}
                onClick={() => inputRef.current?.click()}
                onDragEnter={(e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    setDragActive(true);
                }}
                onDragOver={(e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    setDragActive(true);
                }}
                onDragLeave={(e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    setDragActive(false);
                }}
                onDrop={onDrop}
                role="button"
                tabIndex={0}
            >
                <input
                    ref={inputRef}
                    type="file"
                    multiple
                    accept={accept}
                    className="sr-only"
                    onChange={onPick}
                />

                <div className="flex flex-col sm:flex-row sm:items-center gap-4">
                    <div className="shrink-0 h-12 w-12 rounded-full bg-[var(--primary-bg)] flex items-center justify-center">
                        <Upload className="h-6 w-6 text-[var(--primary-color)]" />
                    </div>

                    <div className="flex-1">
                        <div className={titleText}>Drag & drop files here</div>
                        <div className={helperText + " mt-1"}>
                            Only PDF files · Up to {maxFiles} files · Max {formatBytes(maxSizeBytes)} each
                        </div>
                    </div>
                </div>
            </div>

            {error ? (
                <div className="mt-3 flex items-start gap-2 rounded-[var(--radius-lg)] border border-[var(--danger)] bg-[var(--danger-light)] px-3 py-2 text-sm text-[var(--danger-dark)]">
                    <AlertTriangle className="h-4 w-4 mt-0.5 shrink-0" />
                    <span className="flex-1">{error}</span>
                </div>
            ) : null}

            {items.length > 0 ? (
                <div className="">
                    <div className="flex items-center justify-between mt-4">
                        <p className="text-sm font-semibold text-[var(--text-color)]">
                            Selected files{" "}
                            <span className="text-[var(--text-light)] font-normal">
                ({items.length}/{maxFiles})
              </span>
                        </p>
                        <button
                            type="button"
                            className="text-sm text-[var(--danger-dark)] hover:text-[var(--danger)] transition cursor-pointer"
                            onClick={() => {
                                setItems([]);
                                setError(null);
                            }}
                        >
                            Clear all
                        </button>
                    </div>

                    <div className="mt-3 flex flex-wrap gap-2">
                        {items.map(({ file, id }) => (
                            <span key={id} className={chip}>
                <span className="h-8 w-8 rounded-full bg-[var(--landing-light)] flex items-center justify-center shrink-0">
                  <FileText className="h-4 w-4 text-[var(--primary-color)]" />
                </span>

                <span className="max-w-[220px] truncate text-[var(--text-color)] font-medium">
                  {file.name}
                </span>

                <span className="text-[var(--text-light)] text-xs">{formatBytes(file.size)}</span>

                <button
                    type="button"
                    className="ml-1 inline-flex h-7 w-7 items-center justify-center rounded-full bg-[var(--gray-100)] text-[var(--text-light)] hover:bg-[var(--danger-light)] hover:text-[var(--danger-dark)] transition cursor-pointer"
                    onClick={() => removeById(id)}
                    aria-label={`Remove ${file.name}`}
                >
                  <X className="h-4 w-4" />
                </button>
              </span>
                        ))}
                    </div>
                </div>
            ) : (
                <div className="text-sm text-[var(--text-light)] mt-4">No files selected yet.</div>
            )}
        </div>
    );
}
