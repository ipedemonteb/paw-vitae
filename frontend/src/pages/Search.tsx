import {useEffect, useRef, useState} from "react";
import { Input } from "@/components/ui/input";
import { Search as SearchIcon, Stethoscope, ShieldPlus, ChevronsUpDown, Calendar, List, Grid2X2, Eraser } from "lucide-react";
import { CoverageCombobox } from "@/components/ui/coverage-combobox.tsx";
import { Button } from "@/components/ui/button.tsx";
import { ButtonGroup } from "@/components/ui/button-group.tsx";
import SearchListCard from "@/components/SearchListCard.tsx";
import SearchGridCard from "@/components/SearchGridCard.tsx";
import {useTranslation} from "react-i18next";
import {
    type DoctorQueryParams, type PaginationParams, useDoctorQueryParams,
} from "@/hooks/useQueryParams.ts";
import {useDoctors} from "@/hooks/useDoctors.ts";
import type {PaginationData} from "@/lib/types.ts";
import type {DoctorDTO} from "@/data/doctors.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import PaginationComponent from "@/components/PaginationComponent.tsx";
import {useDebounce} from "use-debounce";
import SearchResultsCard from "@/components/SearchResultCard.tsx";
import {SearchSpecialtyCombobox} from "@/components/SearchSpecialtyCombobox.tsx";
import SearchEmpty from "@/components/SearchEmpty.tsx";
import {SortSelector} from "@/components/SortSelector.tsx";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
const container =
    "px-[24px] mx-auto max-w-6xl w-full";

function transformWeekdays(weekdays: string[]) {
    return weekdays.map(s => Number(s)).filter(n => !Number.isNaN(n));
}

function Search() {
    const doctorsQuery = useDoctorQueryParams();
    const {data: doctors, isLoading: isLoadingDoctors, isError: isErrorDoctors, refetch, isRefetching} = useDoctors({
        specialty: Number(doctorsQuery.specialty),
        coverage: Number(doctorsQuery.coverage),
        weekdays: transformWeekdays(doctorsQuery.weekdays),
        keyword: doctorsQuery.keyword,
        orderBy: doctorsQuery.orderBy,
        direction: doctorsQuery.direction,
        page: doctorsQuery.page
    })

    const loadingDelayed = useDelayedBoolean(isLoadingDoctors, 500)
    const refetchingDelayed = useDelayedBoolean(isRefetching, 500)

    return (
        <div className="bg-(--background-light) min-h-screen pb-10 pt-22">
            <div className={container}>
                <HeroSection searchParams={doctorsQuery} />
                <FilterSection searchParams={doctorsQuery} />
                <ResultSection isRefetching={refetchingDelayed} refetch={refetch} isError={isErrorDoctors} paginationData={doctors} isLoading={loadingDelayed} searchParams={doctorsQuery} />
            </div>
        </div>
    );
}

const heroContainer =
    "relative mt-12 rounded-xl shadow-md py-10 px-8 mb-8 text-center " +
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const heroTitle =
    "text-3xl text-[var(--text-primary)] font-[700] mb-2";
const heroSubtitle =
    "text-lg text-[var(--text-light)] mb-6 font-[300]";
const searchWrapper =
    "relative w-full max-w-2xl mx-auto  rounded-3xl";
const searchHero =
    "w-full bg-white rounded-full pr-6 pl-12 py-5 text-slate-900 placeholder:text-slate-400";

type SectionProps =  {
    searchParams: PaginationParams & {setParams: (updater: (p: URLSearchParams) => void) => void, clearParams: () => void} & DoctorQueryParams
}

function HeroSection({searchParams}: SectionProps) {
    const { t } = useTranslation();

    const [open, setOpen] = useState(false)
    const [keyword, setKeyword] = useState(searchParams.keyword)
    const [focus, setFocus] = useState(false)
    const [debounced] = useDebounce(keyword, 500)
    const specialty = "All Specialties"

    const {data: searchResults, isLoading, isError} = useDoctors({
        keyword: debounced,
        pageSize: 5
    })

    const divRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        const current = searchParams.keyword;
        if (current !== keyword) setKeyword(current);
    }, [searchParams.keyword]);

    useEffect(() => {
        setOpen(keyword.length > 0 && !isLoading && focus && !isError);
    }, [keyword, focus, isLoading]);

    return (
        <div className={heroContainer}>
            <h1 className={heroTitle}>{t("search.title")}</h1>
            <p className={heroSubtitle}>{t("search.subtitle", { specialty: specialty })}</p>
            <div className={searchWrapper}>
                <SearchIcon className="pointer-events-none absolute left-5 top-1/2 h-5 w-5 -translate-y-1/2 text-slate-400" />
                <form
                    onSubmit={(e) => {
                        e.preventDefault();
                        searchParams.setParams((p) => {
                            const val = keyword.trim();
                            if (val.length > 0) p.set("keyword", keyword.trim())
                            else p.delete("keyword")
                        })
                        setOpen(false)
                    }}
                >
                    <Input onBlur={(e) => {
                        const related = e.relatedTarget;
                        if (divRef.current && related && divRef.current.contains(related)) return;
                        setFocus(false);
                    }} onFocus={() => setFocus(true)} type="search" value={keyword} onChange={(e) => (setKeyword(e.currentTarget.value))} placeholder={t("search.search")} className={searchHero} />
                </form>
                <div onMouseDown={(e) => e.preventDefault()} className={`${
                    open
                        ? "opacity-100 translate-y-0 scale-100 pointer-events-auto"
                        : "opacity-0 -translate-y-2 scale-95 pointer-events-none"
                } absolute z-50 max-h-56 scrollbar overscroll-contain overflow-y-auto border rounded-md bg-white w-full p-0 overflow-hidden transition-all duration-200 ease-out origin-top transform`}>
                    {!isLoading && searchResults && searchResults.data.length > 0 && (
                        searchResults.data.map((d) => (
                            <SearchResultsCard doctor={d} key={d.self}/>
                        ))
                    )}
                    {!isLoading && searchResults && searchResults.data.length === 0 && (
                        <div className="h-20 w-full bg-gray-100 flex items-center justify-center gap-1">
                            <span className="text-(--text-light) text-sm">{t("search.searchbar.empty")}</span>
                        </div>
                    )}
                </div>
            </div>

        </div>
    );
}

const filterContainer =
    "flex w-full flex-col gap-4 mb-4 sm:flex-row sm:items-center";
const filterGroup =
    "flex w-full items-center gap-3 text-[var(--text-light)] text-sm flex-1 min-w-0";
const filterLabel =
    "flex items-center gap-1.5 shrink-0";
const filterCombo =
    "w-full flex-1 min-w-0 hover:text-[var(--text-light)] cursor-pointer font-[400]";
const iconSize =
    "h-4 w-4";
const availabilityContainer =
    "flex flex-col mb-6 gap-3";
const availabilityTitle =
    "flex flex-row text-sm items-center gap-1.5 text-[var(--text-light)]";
const availabilityContent =
    "flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between";
const availabilityButtons =
    "flex flex-col gap-4 sm:flex-wrap sm:flex-row";
const availabilityButton =
    "bg-white text-[var(--gray-500)] hover:bg-white border border-[var(--gray-300)] hover:text-[var(--primary-color)] " +
    "hover:border-[var(--primary-color)] data-[selected=true]:bg-[var(--primary-color)] data-[selected=true]:text-white " +
    "data-[selected=true]:border-[var(--primary-color)]";
const availabilityButtonBase =
    "h-10 px-4 rounded-md cursor-pointer";
const applyButton =
    "bg-white text-[var(--primary-color)] border border-[var(--primary-color)] hover:bg-[var(--primary-dark)] " +
    "hover:border-[var(--primary-dark)] hover:text-white px-8 py-5 rounded-md font-[400] text-sm cursor-pointer";

function FilterSection({searchParams}: SectionProps) {
    const { t } = useTranslation();

    const days = ["monday","tuesday","wednesday","thursday","friday","saturday","sunday"] as const;

    const weekdays = transformWeekdays(searchParams.weekdays)

    const toggleDay = (day: number) => {
        searchParams.setParams((p) => {
            const existing = p.getAll("weekdays");
            const strDay = String(day);
            const newVals = existing.includes(strDay) ? existing.filter(v => v !== strDay) : [...existing, strDay];
            p.delete("weekdays");
            newVals.forEach(v => p.append("weekdays", v));
        });
    };

    return (
        <div>
            <div className={filterContainer}>
                <div className={filterGroup}>
                    <div className={filterLabel}>
                        <Stethoscope className={iconSize} />
                        <p>{t("search.specialty.title")}</p>
                    </div>
                    <SearchSpecialtyCombobox
                        searchParams={searchParams}
                        className={filterCombo}
                    />
                </div>
                <div className={filterGroup}>
                    <div className={filterLabel}>
                        <ShieldPlus className={iconSize} />
                        <p>{t("search.coverage.title")}</p>
                    </div>
                    <CoverageCombobox
                        searchParams={searchParams}
                        className={filterCombo}
                    />
                </div>
                <div className={filterGroup}>
                    <div className={filterLabel}>
                        <ChevronsUpDown className={iconSize} />
                        <p>{t("search.sort.title")}</p>
                    </div>
                    <SortSelector
                        searchParams={searchParams}
                        className={filterCombo}
                    />
                </div>
            </div>

            <div className={availabilityContainer}>
                <div className={availabilityTitle}>
                    <Calendar className={iconSize} />
                    <p>{t("search.availability")}</p>
                </div>
                <div className={availabilityContent}>
                    <div className={availabilityButtons}>
                        {days.map((day, index) => (
                            <Button
                                key={day}
                                type="button"
                                variant="outline"
                                data-selected={weekdays.includes(index)}
                                className={`${availabilityButtonBase} ${availabilityButton}`}
                                onClick={() => toggleDay(index)}
                            >
                                {t(`search.week.${day}`)}
                            </Button>
                        ))}
                    </div>
                    <Button onClick={searchParams.clearParams} className={applyButton}>
                        <Eraser/>
                        {t("search.filters")}
                    </Button>
                </div>
            </div>
        </div>
    );
}

const resultHeader =
    "flex flex-row items-center justify-between gap-4 mb-4";
const resultText =
    "text-sm text-[var(--text-light)]";
const formatBtnBase =
    "bg-white border border-[var(--primary-color)] cursor-pointer";
const formatBtnActive =
    "bg-[var(--primary-color)] text-white hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)]";
const formatBtnInactive =
    "text-[var(--primary-color)] hover:bg-[var(--gray-100)] hover:text-[var(--primary-dark)]";

type ResultSectionProps = {
    paginationData?: PaginationData<DoctorDTO[]>
    isLoading: boolean
    isRefetching: boolean
    isError: boolean
    refetch?: () => void
    searchParams: PaginationParams & {setParams: (updater: (p: URLSearchParams) => void) => void}
}

function ResultSection({paginationData, isLoading, isRefetching, isError, refetch, searchParams}: ResultSectionProps ) {
    const { t } = useTranslation();

    const [view, setView] = useState<"list" | "grid">("list");

    return (
        <div>
            <div className={resultHeader}>
                <p className={isLoading ? resultText + " invisible" : resultText}>{t("search.found", { doctorsFound: paginationData?.pagination.total })}</p>
                <div>
                    <ButtonGroup orientation="horizontal">
                        <Button
                            type="button"
                            aria-label="view-list"
                            onClick={() => setView("list")}
                            className={`${formatBtnBase} ${view === "list" ? formatBtnActive : formatBtnInactive}`}
                        >
                            <List />
                        </Button>
                        <Button
                            type="button"
                            aria-label="view-grid"
                            onClick={() => setView("grid")}
                            className={`${formatBtnBase} ${view === "grid" ? formatBtnActive : formatBtnInactive}`}
                        >
                            <Grid2X2 />
                        </Button>
                    </ButtonGroup>
                </div>
            </div>
            {view === "list" ? isLoading ? (
                <div className="h-full w-full flex flex-col gap-4 pb-6">
                    {Array.from({length: 9}).map((_, i) => (
                        <Skeleton key={i} className="h-52 w-full"/>
                        ))}
                </div>

            ) : isError ? (
                <SearchEmpty isRefetching={isRefetching} error={true} refetch={refetch}/>
            ) : (
                <ResultList data={paginationData?.data}/>
            ) : isLoading ? (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
                    {Array.from({length: 5}).map((_, i) => (
                        <Skeleton key={i} className="h-92 w-88 p-0 gap-0"/>
                    ))}
                </div>
            ) : isError ? (
                <SearchEmpty isRefetching={isRefetching} error={true} refetch={refetch}/>
            ) : (
                <ResultGrid data={paginationData?.data}/>
            )
            }
            <PaginationComponent pagination={paginationData?.pagination} searchParams={searchParams}/>
        </div>
    );
}

const resultContentList =
    "flex flex-col gap-4 pb-6";

type ResultProps = {
    data?: DoctorDTO[]
}

function ResultList({data}: ResultProps) {
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(false);
        const id = requestAnimationFrame(() => setMounted(true));
        return () => cancelAnimationFrame(id);
    }, [data]);

    if (data?.length === 0) {
        return (
            <SearchEmpty error={false}/>
        )
    }
    return (
        <div className={resultContentList}>
            {data?.map((d, i) => (
                <div
                    key={d.self}
                    style={{ transitionDelay: `${i * 80}ms` }}
                    className={`transform transition-all duration-300 ${mounted ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-4'}`}
                >
                    <SearchListCard doctor={d} />
                </div>
            ))}
        </div>
    )
}

const resultContentGrid =
    "grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-6";

function ResultGrid({data}: ResultProps) {
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(false);
        const id = requestAnimationFrame(() => setMounted(true));
        return () => cancelAnimationFrame(id);
    }, [data]);

    if (data?.length === 0) {
        return (
            <SearchEmpty error={false}/>
        )
    }
    return (
        <div className={resultContentGrid}>
            {data?.map((d, i) => (
                <div
                    key={d.self}
                    style={{ transitionDelay: `${i * 60}ms` }}
                    className={`transform transition-all duration-300 ${mounted ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-4 scale-95'}`}
                >
                    <SearchGridCard doctor={d} key={d.self} />
                </div>
            ))}
        </div>
    )
}


export default Search;