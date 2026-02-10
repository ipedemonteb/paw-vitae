import {describe, it, expect} from "vitest";
import {renderHook, waitFor} from "@/__test__/setup/utils.tsx";
import {useNeighborhood, useNeighborhoods} from "@/hooks/useNeighborhoods.ts";


describe('UseNeighborhoods', () => {

    describe('useNeighborhoods hook', () => {
        it('should return a list of neighborhoods', async () => {
            const {result} = renderHook(() => useNeighborhoods());

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();

            expect(result.current.data).toHaveLength(3);

            const firstCoverage = result.current.data![0];
            expect(firstCoverage.name).toBe('Belgrano');
            expect(firstCoverage.self).toContain('/neighborhoods/1');

        })
    })

    describe('useNeighborhood hook', () => {
        it('should return a single neighborhood', async () => {

            const {result} = renderHook(() => useNeighborhood('/neighborhoods/1'));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();

            expect(result.current.data?.name).toBe('Belgrano');
            expect(result.current.data?.self).toContain('/neighborhoods/1');

        })

        it('should return error if there is no such neighborhood', async () => {
            const {result} = renderHook(() => useNeighborhood('/neighborhoods/error'));
            await waitFor(() => expect(result.current.isError).toBe(true));
        })


    })

})