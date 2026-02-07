import {describe, it, expect} from "vitest";
import {renderHook, waitFor} from "@/__test__/setup/utils.tsx";
import {useCoverage, useCoverages} from "@/hooks/useCoverages.ts";


describe('useCoverages', () => {

    describe('useCoverages hook', () => {
        it('deberia retornar una lista de coverages', async () => {
            const {result} = renderHook(() => useCoverages());

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();

            expect(result.current.data).toHaveLength(3);

            const firstCoverage = result.current.data![0];
            expect(firstCoverage.name).toBe('Medifé');
            expect(firstCoverage.self).toContain('/coverages/1');

        })
    })

    describe('useCoverage hook', () => {
        it('deberia retornar un unico coverage', async () => {

            const {result} = renderHook(() => useCoverage('/coverages/1'));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();

            expect(result.current.data?.name).toBe('Medifé');
            expect(result.current.data?.self).toContain('/coverages/1');

        })

        it('deberia retornar error si no existe el coverage', async () => {
            const {result} = renderHook(() => useCoverage('/coverages/error'));
            await waitFor(() => expect(result.current.isError).toBe(true));
        })


    })

})